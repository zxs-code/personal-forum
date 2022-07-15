package com.github.code.zxs.search.model.document;

import com.github.code.zxs.search.model.bean.Searchable;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 注解：@Document用来声明Java对象与ElasticSearch索引的关系
 * indexName 索引名称
 * type      索引类型
 * shards    主分区数量，默认5
 * replicas  副本分区数量，默认1
 * createIndex 索引不存在时，是否自动创建索引，默认true
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@FieldNameConstants
@Document(indexName = "posts")
public class PostsDocument implements Searchable {
    @Id
    private Long id;
    /**
     * 标题，type默认是
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    /**
     * 摘要
     */
    @Field(type = FieldType.Keyword)
    private String summary;
    /**
     * 是否置顶
     */
    @Field(type = FieldType.Boolean)
    private Boolean top;
    /**
     * 是否精华
     */
    @Field(type = FieldType.Boolean)
    private Boolean good;
    /**
     * 是否锁定，锁定后帖子为只读，不能进行更新或删除操作，也不能回复。
     */
    @Field(type = FieldType.Boolean)
    private Boolean locked;
    /**
     * 是否隐藏，隐藏后需要相应的权限才能阅览
     */
    @Field(type = FieldType.Boolean)
    private Boolean hide;
    /**
     * 是否匿名
     */
    @Field(type = FieldType.Boolean)
    private Boolean anonymous;
    /**
     * 浏览量
     */
    @Field(type = FieldType.Long)
    private Long views;
    /**
     * 浏览人数
     */
    @Field(type = FieldType.Long)
    private Long visitor;
    /**
     * 点赞数
     */
    @Field(type = FieldType.Long)
    private Long likes;
    /**
     * 点踩数
     */
    @Field(type = FieldType.Long)
    private Long dislikes;
    /**
     * 评论数
     */
    @Field(type = FieldType.Long)
    private Long comment;
    /**
     * 收藏数
     */
    @Field(type = FieldType.Long)
    private Integer stars;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(name = "create_time", type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ || epoch_millis")
    private Date createTime;

    @Field(name = "update_time", type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ || epoch_millis")
    private Date updateTime;

    @Field(name = "delete_time", type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ || epoch_millis")
    private Date deleteTime;

    @Field(name = "create_by", type = FieldType.Long)
    private Long createBy;

    /**
     * 预览图地址
     */
    @Field(type = FieldType.Keyword)
    private String previewImg;
    /**
     * 预览图原图地址
     */
    @Field(type = FieldType.Keyword)
    private String srcImg;

    public Boolean getAnonymous() {
        return Boolean.TRUE.equals(anonymous);
    }

    @Override
    public String getDocumentId() {
        return id.toString();
    }
}