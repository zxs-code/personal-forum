package com.github.code.zxs.search.model.bean;

import com.github.code.zxs.search.model.enums.OperatorEnum;
import lombok.Getter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Filter {
    private final List<Expression> expressions;
    private final List<Logic> logicList;

    public enum Logic {
        AND,
        OR,
        NOT
    }

    @Getter
    public static class Expression {
        private final String fieldName;
        private final OperatorEnum operator;
        private final Object v1;
        private final Object v2;

        public Expression(String fieldName, OperatorEnum operator, Object v1, Object v2) {
            Objects.requireNonNull(fieldName, "属性名不能为空");
            Objects.requireNonNull(operator, "操作符不能为空");
            this.fieldName = fieldName;
            this.operator = operator;
            this.v1 = v1;
            this.v2 = v2;
        }

        public static Expression equals(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.EQUALS, value1, null);
        }

        public static Expression notEquals(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.NOT_EQUALS, value1, null);
        }

        public static Expression between(String fieldName, Object value1, Object value2) {
            return new Expression(fieldName, OperatorEnum.BETWEEN, value1, value2);
        }

        public static Expression gt(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.GT, value1, null);
        }

        public static Expression gte(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.GTE, value1, null);
        }

        public static Expression lt(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.LT, value1, null);
        }

        public static Expression lte(String fieldName, Object value1) {
            return new Expression(fieldName, OperatorEnum.LTE, value1, null);
        }

        public QueryBuilder getQueryBuilder() {
            return queryBuilder();
        }

        private QueryBuilder queryBuilder() {
            switch (operator) {
                case EQUALS:
                    return v1 == null
                            ? QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(fieldName))
                            : QueryBuilders.termQuery(fieldName, v1);
                case NOT_EQUALS:
                    return v1 == null
                            ? QueryBuilders.existsQuery(fieldName)
                            : QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(fieldName, v1));
                case BETWEEN:
                    return QueryBuilders.rangeQuery(fieldName).from(v1).to(v2);
                case GT:
                    return QueryBuilders.rangeQuery(fieldName).gt(v1);
                case GTE:
                    return QueryBuilders.rangeQuery(fieldName).gte(v1);
                case LT:
                    return QueryBuilders.rangeQuery(fieldName).lt(v1);
                case LTE:
                    return QueryBuilders.rangeQuery(fieldName).lte(v1);
            }
            return null;
        }
    }

    public Filter() {
        this.expressions = new ArrayList<>();
        this.logicList = new ArrayList<>();
    }

    public Filter(String fieldName, OperatorEnum operator, Object v1, Object v2) {
        this(new Expression(fieldName, operator, v1, v2));
    }

    public Filter(Expression e) {
        this();
        and(e);
    }

    public Filter(List<Expression> expressions) {
        this();
        for (Expression expression : expressions) and(expression);
    }

    public Filter and(Expression e) {
        addExpression(e);
        addLogic(Logic.AND);
        return this;
    }

    public Filter or(Expression e) {
        addExpression(e);
        addLogic(Logic.OR);
        return this;
    }

    public Filter not(Expression e) {
        addExpression(e);
        addLogic(Logic.NOT);
        return this;
    }

    private void addExpression(Expression e) {
        Objects.requireNonNull(e, "表达式不能为null");
        expressions.add(e);
    }

    private void addLogic(Logic logic) {
        Objects.requireNonNull(logic, "逻辑不能为null");
        logicList.add(logic);
    }


    public QueryBuilder getQueryBuilder() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (int i = 0; i < expressions.size(); i++) {
            QueryBuilder subQuery = expressions.get(i).getQueryBuilder();
            switch (logicList.get(i)) {
                case AND:
                    queryBuilder.must(subQuery);
                    break;
                case OR:
                    queryBuilder.should(subQuery);
                    break;
                case NOT:
                    queryBuilder.mustNot(subQuery);
                    break;
            }
        }
        return queryBuilder;
    }

    public int getTotalExpression() {
        return expressions.size();
    }
}
