package com.github.code.zxs.search.model.bean;

import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.search.model.enums.OrderDirEnum;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SearchSort {
    public static final Map<Class<?>, Map<String, Class<?>>> CACHE = new ConcurrentHashMap<>();
    private final Class<?> cls;
    private final String orderField;
    private final OrderDirEnum orderDir;

    public SearchSort(Class<?> cls, String orderField, OrderDirEnum orderDir) {
        Objects.requireNonNull(cls, "排序类不能为空");
        Objects.requireNonNull(cls, "排序字段不能为空");
        Objects.requireNonNull(cls, "排序方向不能为空");
        Optional.ofNullable(CACHE.computeIfAbsent(cls, BeanUtils::resolveFieldMap).get(orderField))
                .orElseThrow(() -> new IllegalArgumentException(cls + "类中不存在" + orderField));
        this.cls = cls;
        this.orderField = orderField;
        this.orderDir = orderDir;
    }

    public SearchSort(Class<?> cls, String orderField, int orderDir) {
        this(cls, orderField, OrderDirEnum.valueOf(orderDir));
    }

    public SearchSort(Class<?> cls, String orderField) {
        this(cls, orderField, OrderDirEnum.ASC);
    }

    public org.springframework.data.domain.Sort getSort() {
        return org.springframework.data.domain.Sort.by(orderDir == OrderDirEnum.ASC ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC, orderField);
    }
}
