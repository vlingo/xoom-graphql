package io.vlingo.graphql.model;

import graphql.schema.DataFetcher;

/**
 * GraphQL abstract query.
 */
public class GqlAbstractQuery {
    private final String fieldName;
    private final DataFetcher<?> dataFetcher;

    protected GqlAbstractQuery(String fieldName, DataFetcher<?> dataFetcher) {
        this.fieldName = fieldName;
        this.dataFetcher = dataFetcher;
    }

    public String getFieldName() {
        return fieldName;
    }

    public DataFetcher<?> getDataFetcher() {
        return dataFetcher;
    }
}
