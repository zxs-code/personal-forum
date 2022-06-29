package com.github.code.zxs.search.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.enums.OrderDirEnum;
import com.github.code.zxs.search.model.enums.OrderEnum;
import io.jsonwebtoken.lang.Collections;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SortFactory {
    private static final Map<OrderEnum, Sort> SORT_STRATEGY;

    static {
        SORT_STRATEGY = Arrays.stream(OrderEnum.values())
                .collect(Collectors.toMap(order -> order, order -> Sort.by(Sort.Direction.valueOf(order.getOrderDir().name()), order.getOrderBy())));
    }

    public static Sort getSort(ResourceTypeEnum type, OrderEnum orderEnum) {
        if (type == null || orderEnum == null || type != orderEnum.getType())
            return Sort.unsorted();
        return SORT_STRATEGY.getOrDefault(orderEnum, Sort.unsorted());
    }

    public static Sort getSort(Map<String, OrderDirEnum> map) {
        if (Collections.isEmpty(map))
            return Sort.unsorted();
        List<Sort.Order> orders = map.entrySet().stream().map(entry -> new Sort.Order(Sort.Direction.valueOf(entry.getValue().name()), entry.getKey())).collect(Collectors.toList());
        return Sort.by(orders);
    }
}
