package com.github.code.zxs.search.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.bean.Filter;
import com.github.code.zxs.search.model.enums.FilterEnum;
import com.github.code.zxs.search.model.enums.OperatorEnum;
import io.jsonwebtoken.lang.Collections;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilterFactory {
    private static final Map<FilterEnum, Filter.Expression> OPTIONAL_EXPRESSIONS;
    private static final Map<ResourceTypeEnum, List<Filter.Expression>> DEFAULT_EXPRESSIONS;

    private static Filter.Expression enumToExpression(FilterEnum filter) {
        return new Filter.Expression(filter.getFieldName(), filter.getOperatorEnum(), filter.getV1(), filter.getV2());
    }

    static {
        OPTIONAL_EXPRESSIONS = Arrays.stream(FilterEnum.values())
                .filter(f -> !f.isDefaultOption())
                .collect(Collectors.toMap(filter -> filter, FilterFactory::enumToExpression));
        DEFAULT_EXPRESSIONS = Arrays.stream(FilterEnum.values())
                .filter(FilterEnum::isDefaultOption)
                .collect(Collectors.groupingBy(FilterEnum::getType, Collectors.mapping(FilterFactory::enumToExpression, Collectors.toList())));
    }

    public static Filter getFilter(ResourceTypeEnum type, FilterEnum... filterEnum) {
        Filter filter = new Filter(DEFAULT_EXPRESSIONS.get(type));
        if (type == null || filterEnum == null)
            return filter;

        Arrays.stream(filterEnum)
                .filter(Objects::nonNull)
                .filter(f -> f.getType() == type)
                .map(OPTIONAL_EXPRESSIONS::get)
                .filter(Objects::nonNull)
                .forEach(filter::and);

        return filter;
    }


    public static Filter getMustFilter(Map<String, Object> filterMap) {
        Filter filter = new Filter();
        if (Collections.isEmpty(filterMap))
            return filter;

        filterMap.forEach((fieldName, fieldValue) -> filter.and(new Filter.Expression(fieldName, OperatorEnum.EQUALS, fieldValue, null)));
        return filter;
    }

    public static Filter getFilter(List<Filter.Expression> expressions) {
        Filter filter = new Filter();
        if (Collections.isEmpty(expressions))
            return filter;

        expressions.forEach(filter::and);
        return filter;
    }

}
