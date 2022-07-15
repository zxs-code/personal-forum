package com.github.code.zxs.search.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.code.zxs.core.constant.MysqlTypeConstant;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.model.bean.CanalBean;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.DateUtils;
import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.core.util.StringUtils;
import com.github.code.zxs.resource.model.entity.Posts;
import com.github.code.zxs.search.model.bean.Searchable;
import com.github.code.zxs.search.model.document.PostsDocument;
import com.github.code.zxs.search.model.document.UserDocument;
import com.github.code.zxs.search.service.manager.SearchManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.sql.Types;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 订阅canal的消息，同步MySQL的数据，null值也会同步
 * MySQL布尔类型需要使用bool或boolean，不能使用tinyint(1)
 */
@Component
public class CanalConsumer {
    public static final int batchSize = 1000;
    @Autowired
    private SearchManager searchManager;

    @KafkaListener(topics = TopicConstant.FORUM_TB_POSTS, groupId = "es", containerFactory = "canalManualBatchFactory")
    public void syncPostsBody(List<String> list, Consumer consumer) {

        Map<Boolean, List<Document>> map = convertAndSplitDocument(list);

        for (Document document : map.get(true)) {
            processPostsBody(document);
        }
        searchManager.batchSave(map.get(true), PostsDocument.class, batchSize);
        searchManager.batchDelete(map.get(false), PostsDocument.class, batchSize);
        consumer.commitSync();
    }

    @KafkaListener(topics = TopicConstant.FORUM_TB_POSTS_DATA,
            groupId = "es", containerFactory = "canalManualBatchFactory")
    public void syncPostsData(List<String> list, Consumer consumer) {
        batchProcess(list, PostsDocument.class);
        consumer.commitSync();
    }

    /**
     * 与{@link CanalConsumer#syncPostsData(List, Consumer)}操作的文档相同，为了统一去重操作，分开消费
     *
     * @param list
     * @param consumer
     */
    @KafkaListener(topics = TopicConstant.FORUM_TB_POSTS_STATE,
            groupId = "es", containerFactory = "canalManualBatchFactory")
    public void syncPostsState(List<String> list, Consumer consumer) {
        batchProcess(list, PostsDocument.class);
        consumer.commitSync();
    }

    @KafkaListener(topics = TopicConstant.FORUM_TB_USER,
            groupId = "es", containerFactory = "canalManualBatchFactory")
    public void syncUser(List<String> list, Consumer consumer) {
        batchProcess(list, UserDocument.class);
        consumer.commitSync();
    }

    @KafkaListener(topics = TopicConstant.FORUM_TB_USER_INFO,
            groupId = "es", containerFactory = "canalManualBatchFactory")
    public void syncUserInfo(List<String> list, Consumer consumer) {
        batchProcess(list, UserDocument.class);
        consumer.commitSync();
    }


    /**
     * 反序化数据库中的第一个数组字符串
     *
     * @param arrayJson
     * @return
     */
    private String processJsonArray(String arrayJson) {
//        return arrayJson.replaceAll("\"(\\[.*])\"", "$1").replaceAll("\\\\\"","\"");

        Matcher matcher = Pattern.compile("\"\\[.*?]\"").matcher(arrayJson);
        if (matcher.find()) {
            String newStr = JsonUtils.parse(matcher.group(0), String.class);
            Objects.requireNonNull(newStr);
            return matcher.replaceFirst(newStr);
        }
        return arrayJson;
    }

    /**
     * 处理帖子的标签和概要
     *
     * @param posts
     */
    private void processPostsBody(Document posts) {
        String content = posts.getString(Posts.Fields.content);
        if (StringUtils.isNotEmpty(content))
            posts.put(PostsDocument.Fields.summary, content.substring(0, Math.min(content.length(), 320)));
        String tagsField = PostsDocument.Fields.tags;
        posts.put(tagsField, JsonUtils.parseList(posts.getString(tagsField), String.class));
    }

    private Document mapToDocument(Map<String, Object> map) {
        Document document = Document.from(map);
        document.setId(document.get("id").toString());
        return document;
    }

    @Nullable
    private CanalBean<Map<String, Object>> resolveCanalJson(String canalJson) {
        if (StringUtils.isEmpty(canalJson))
            return null;
        CanalBean<Map<String, Object>> canalBean = JsonUtils.parse(canalJson, new TypeReference<CanalBean<Map<String, Object>>>() {
        });
        assert canalBean != null;
        Map<String, Integer> sqlType = canalBean.getSqlType();
        Map<String, String> mysqlType = canalBean.getMysqlType();

        //修改MySQL中的布尔类型对应的sqlType
        if (CollectionUtils.isNotEmpty(mysqlType)) {
            for (Map.Entry<String, String> entry : mysqlType.entrySet()) {
                String value = entry.getValue();
                if (MysqlTypeConstant.BOOL.equals(value) || MysqlTypeConstant.BOOLEAN.equals(value)) {
                    sqlType.put(entry.getKey(), Types.BOOLEAN);
                }
            }
        }

        convertCanalMapType(canalBean.getData(), sqlType);
        convertCanalMapType(canalBean.getOld(), sqlType);
        return canalBean;
    }


    private void convertCanalMapType(List<Map<String, Object>> list, Map<String, Integer> sqlType) {
        //实际类型为List<Map<String,String>>
        if (CollectionUtils.isEmpty(list))
            return;

        for (Map<String, Object> map : list)
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() == null)
                    continue;
                else if (!(entry.getValue() instanceof String))
                    throw new IllegalArgumentException("canal对象解析错误，value值不是String类型");
                String value = (String) entry.getValue();

                switch (sqlType.get(entry.getKey())) {
                    case Types.TINYINT:
                    case Types.SMALLINT:
                    case Types.INTEGER:
                        entry.setValue(Integer.parseInt(value));
                        break;
                    case Types.BIGINT:
                        entry.setValue(Long.parseLong(value));
                        break;
                    case Types.FLOAT:
                    case Types.DOUBLE:
                        entry.setValue(Double.parseDouble(value));
                        break;
                    case Types.BOOLEAN:
                        entry.setValue("1".equals(value));
                        break;
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        entry.setValue(DateUtils.formatZonedDate(DateUtils.parseBasicDate(value)));
                        break;
                }
            }
    }

    /**
     * 批量去重保存，文档id相同仅保留最后一个
     *
     * @param canalJsonList
     * @param cls
     */
    private void batchProcess(List<String> canalJsonList, Class<? extends Searchable> cls) {

        Map<Boolean, List<Document>> map = convertAndSplitDocument(canalJsonList);
        //批量更新
        searchManager.batchSave(map.get(true), cls, batchSize);
        //批量删除
        searchManager.batchDelete(map.get(false), cls, batchSize);
    }

    /**
     * 将document分为保存和删除两部分，并根据文档id进行去重
     * 新增或更新为true，删除为false
     *
     * @param canalJsonList
     * @return
     */
    private Map<Boolean, List<Document>> convertAndSplitDocument(List<String> canalJsonList) {
        Map<Boolean, List<CanalBean<Map<String, Object>>>> map = canalJsonList.stream()
                .map(this::resolveCanalJson)
                .filter(Objects::nonNull)
                .filter(CanalBean::isDml)
                .collect(Collectors.partitioningBy(CanalBean::isInsertOrUpdate));
        Map<Boolean, List<Document>> hashMap = new HashMap<>();
        hashMap.put(true, beanListToDistinctDocument(map.get(true)));
        hashMap.put(false, beanListToDistinctDocument(map.get(false)));
        return hashMap;
    }

    private List<Document> beanListToDistinctDocument(List<CanalBean<Map<String, Object>>> list) {
        List<Document> documents = list.stream()
                .map(CanalBean::getData)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(this::mapToDocument)
                .collect(Collectors.toList());
        return distinct(documents);
    }


    /**
     * 根据文档id去重,保留相同id的最后一个
     *
     * @param documents
     * @return
     */
    private List<Document> distinct(List<Document> documents) {
        LinkedHashMap<String, Document> linkedHashMap = new LinkedHashMap<>();
        for (Document document : documents) {
            linkedHashMap.put(document.getId(), document);
        }
        return new ArrayList<>(linkedHashMap.values());
    }


}
