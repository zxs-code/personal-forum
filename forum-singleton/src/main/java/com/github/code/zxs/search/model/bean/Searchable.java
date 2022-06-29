package com.github.code.zxs.search.model.bean;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * es文档的接口
 * 默认创建的索引结构不包含 用 {@link Field#type()} 为 {@link FieldType#Auto} 修饰的属性。
 * 当文档数据为空时，索引结构不会改变，搜索文档时使用以上属性会出现异常。
 */
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public interface Searchable {
    String getDocumentId();
}
