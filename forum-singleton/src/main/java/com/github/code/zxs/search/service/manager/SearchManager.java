package com.github.code.zxs.search.service.manager;

import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.core.util.StringUtils;
import com.github.code.zxs.search.model.bean.Filter;
import com.github.code.zxs.search.model.bean.Searchable;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 新增或更新操作的逻辑删除过滤由MySQL完成，es通过canal订阅MySQL的binlog同步数据
 */
@Component
public class SearchManager {
    public static final String CREATE_TIME = "createTime";
    public static final String DELETE_TIME = "deleteTime";
    /**
     * 本地缓存
     */
//    private Map<Class<? extends Searchable>, Cache<String, Object>> CACHE_MAP;


    @Autowired
    private SearchManager self;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @PostConstruct
    public void init() {
        Set<Class<? extends Searchable>> subClasses = BeanUtils.getSubClasses("com.github.code.zxs", Searchable.class);
        for (Class<?> c : subClasses) {
            IndexOperations indexOps = restTemplate.indexOps(c);
            if (!indexOps.exists()) {
                indexOps.create();
                indexOps.putMapping(indexOps.createMapping());
            }
        }
//        CACHE_MAP = new ConcurrentHashMap<>();
//        CACHE_MAP.put(UserDocument.class, CacheUtils.getSimpleLocalCache(10000, Duration.ofMinutes(10L)));
    }

    /**
     * 根据id查询
     *
     * @param id  文档id
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends Searchable> T get(String id, Class<T> cls) {
        List<T> list = get(CollectionUtils.asList(id), cls);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据id查询
     *
     * @param ids
     * @param cls
     * @param <T>
     * @return 返回对应的文档集合，若为空则返回空集合
     */
    public <T extends Searchable> List<T> get(List<String> ids, Class<T> cls) {
        Objects.requireNonNull(ids, "查询的id集合不能为空");
        Objects.requireNonNull(cls, "查询的文档类型不能为空");

        List<String> noCacheIds = ids.stream().filter(Objects::nonNull).collect(Collectors.toList());
//        Cache<String, T> cache = getCache(cls);
        Map<String, T> cacheMap = null;
//        if (cache != null) {
//            cacheMap = cache.getAllPresent(noCacheIds);
//            if (CollectionUtils.isNotEmpty(cacheMap)) {
//                noCacheIds.removeAll(cacheMap.keySet());
//            }
//        }

        String[] idList = noCacheIds.stream().map(Object::toString).toArray(String[]::new);

        //过滤逻辑删除的文档
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.idsQuery().addIds(idList));

        NativeSearchQuery query = new NativeSearchQuery(boolQueryBuilder);
        SearchHits<T> searchHits = restTemplate.search(query, cls);
        Map<String, T> searchMap = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toMap(Searchable::getDocumentId, d -> d));

//        if (cache != null)
//            cache.putAll(searchMap);
        Map<String, T> finalCacheMap = cacheMap;

        return ids.stream()
                .map(id -> id == null ? null : Optional.ofNullable(finalCacheMap).map(m -> m.get(id)).orElseGet(() -> searchMap.get(id)))
                .collect(Collectors.toList());
    }

    /**
     * 新增
     *
     * @param document
     * @param <T>
     * @return
     */
    public <T extends Searchable> T save(T document) {
        return restTemplate.save(document);
    }

    /**
     * 更新
     *
     * @param document
     * @return
     */
    public boolean update(Searchable document) {
        UpdateResponse response = restTemplate.update(UpdateQuery
                .builder(document.getDocumentId())
                .withDocAsUpsert(true)
                .withRetryOnConflict(1)
                .withDocument(Document.parse(Objects.requireNonNull(JsonUtils.serialize(document))))
                .build(), restTemplate.getIndexCoordinatesFor(document.getClass()));
        return response.getResult() == UpdateResponse.Result.UPDATED;
    }

    /**
     * 根据id删除
     *
     * @param id
     * @param cls
     * @return
     */
    public String delete(String id, Class<?> cls) {
        return restTemplate.delete(id, cls);
    }

    /**
     * 批量保存，更新的索引为集合中第一个文档对应的索引
     *
     * @param searchable 文档集合
     * @param batchSize  每个批次的数量
     */
    public void batchSave(List<? extends Searchable> searchable, int batchSize) {
        if (CollectionUtils.isEmpty(searchable))
            return;
        List<Document> documents = new ArrayList<>();
        for (Searchable s : searchable) {
            Document document = Document.from(JsonUtils.toMapWithNullAndUnderline(s));
            document.setId(s.getDocumentId());
            documents.add(document);
        }
        self.batchSave(documents, searchable.get(0).getClass(), batchSize);
    }

    /**
     * 批量保存，更新的索引为集合中第一个文档对应的索引
     *
     * @param documents 文档集合
     * @param batchSize 每个批次的数量
     */
    public void batchSave(List<Document> documents, Class<? extends Searchable> cls, int batchSize) {
        IndexCoordinates index = restTemplate.getIndexCoordinatesFor(cls);
        List<List<Document>> list = CollectionUtils.subList(documents, batchSize);
        List<UpdateQuery> queries = new ArrayList<>();
        UpdateQuery updateQuery;
        for (List<Document> subList : list) {
            for (Document document : subList) {
                updateQuery = UpdateQuery
                        .builder(document.getId())
                        .withDocAsUpsert(true)
                        .withRetryOnConflict(1)
                        .withDocument(document)
                        .build();
                queries.add(updateQuery);
            }
            //分批提交索引
            restTemplate.bulkUpdate(queries, index);
            queries.clear();
        }
    }

    /**
     * 批量删除，删除的索引为集合中第一个文档对应的索引
     *
     * @param idList    文档集合
     * @param cls       文档类
     * @param batchSize 每个批次的数量
     */
    public void batchDeleteByIdList(List<String> idList, Class<? extends Searchable> cls, int batchSize) {
        IndexCoordinates index = restTemplate.getIndexCoordinatesFor(cls);
        List<List<String>> list = CollectionUtils.subList(idList, batchSize);

        for (List<String> subList : list) {
            IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds(subList.toArray(new String[0]));
            NativeSearchQuery query = new NativeSearchQuery(idsQueryBuilder);
            //分批提交索引
            restTemplate.delete(query, cls, index);
        }
    }

    /**
     * 根据文档id批量删除，删除的索引为集合中第一个文档对应的索引
     *
     * @param documents 文档集合
     * @param cls       文档类
     * @param batchSize 每个批次的数量
     */
    public void batchDelete(List<Document> documents, Class<? extends Searchable> cls, int batchSize) {
        List<String> idList = documents.stream().map(Document::getId).collect(Collectors.toList());
        batchDeleteByIdList(idList, cls, batchSize);
    }

    /**
     * 搜索所有文档，结果集按时间倒序排序
     *
     * @param pageDTO    页面参数
     * @param cls        文档类型
     * @param fieldNames 检索的属性名
     * @return 页面结果集
     */
    public <T extends Searchable> PageResult<T> pageMatchAllQuery(PageDTO pageDTO, Class<T> cls, String... fieldNames) {
        return self.pageMultiMatchQuery(pageDTO, cls, null, null, null, null, fieldNames);
    }


    /**
     * 按关键字检索文档,结果集按时间倒序排序
     *
     * @param pageDTO    页面参数
     * @param cls        文档类型
     * @param filter     过滤器
     * @param sort       排序方式
     * @param text       搜索的关键字
     * @param fieldNames 检索的属性名
     * @return 页面结果集
     */
    public <T extends Searchable> PageResult<T> pageMultiMatchQuery(PageDTO pageDTO,
                                                                    Class<T> cls,
                                                                    @Nullable Filter filter,
                                                                    @Nullable Sort sort,
                                                                    @Nullable SourceFilter sourceFilter,
                                                                    @Nullable String text,
                                                                    String... fieldNames) {
        SearchHits<T> searchHits = self.search(pageDTO, cls, filter, sort, sourceFilter, text, fieldNames);
        List<T> item = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PageResult<>(pageDTO, searchHits.getTotalHits(), item);
    }

    /**
     * 按关键字检索文档
     *
     * @param pageDTO    页面参数
     * @param cls        文档类型
     * @param filter     过滤器
     * @param sort       排序方式，默认按创建时间顺序排序
     * @param text       搜索的关键字
     * @param fieldNames 检索的属性名 text为null时可为空
     * @return 原始SearchHits
     */
    public <T extends Searchable> SearchHits<T> search(PageDTO pageDTO,
                                                       Class<T> cls,
                                                       @Nullable Filter filter,
                                                       @Nullable Sort sort,
                                                       @Nullable SourceFilter sourceFilter,
                                                       @Nullable String text,
                                                       String... fieldNames) {

        QueryBuilder queryBuilder = StringUtils.isEmpty(text)
                ? QueryBuilders.matchAllQuery()
                : QueryBuilders.multiMatchQuery(text, fieldNames);

        Query query = new NativeSearchQuery(queryBuilder, filter == null ? null : filter.getQueryBuilder())
                .setPageable(PageRequest.of(pageDTO.getCurPage().intValue() - 1, pageDTO.getPageSize().intValue()))
                .addSort(sort);
        if (sourceFilter != null)
            query.addSourceFilter(sourceFilter);
        return restTemplate.search(query, cls);
    }

//    @SuppressWarnings("all")
//    private <T> Cache<String, T> getCache(Class<T> cls) {
//        return (Cache<String, T>) CACHE_MAP.get(cls);
//    }

}
