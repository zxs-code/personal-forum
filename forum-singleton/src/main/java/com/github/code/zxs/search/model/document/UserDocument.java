package com.github.code.zxs.search.model.document;

import com.github.code.zxs.auth.model.enums.UserStateEnum;
import com.github.code.zxs.resource.model.enums.GenderEnum;
import com.github.code.zxs.search.model.bean.Searchable;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

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
@Document(indexName = "user")
public class UserDocument implements Searchable {
    @Id
    private Long id;
    /**
     * 用户名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String username;
    /**
     * 手机号码
     */
    @Field(type = FieldType.Keyword)
    private String phone;
    /**
     * 邮箱
     */
    @Field(type = FieldType.Keyword)
    private String email;
    /**
     * 昵称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String nickname;
    /**
     * 性别，枚举需要自己写转换器
     */
    @Field(type = FieldType.Integer)
    private GenderEnum gender;
    /**
     * 出生年月
     */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ || epoch_millis")
    private Date birthday;
    /**
     * 个性签名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String signature;
    /**
     * 头像url
     */
    @Field(type = FieldType.Keyword)
    private String avatar;

    /**
     * 账号状态
     */
    @Field(type = FieldType.Integer)
    private UserStateEnum state;

    @Field(type = FieldType.Long)
    private Long fans;

    @Override
    public String getDocumentId() {
        return id.toString();
    }
}