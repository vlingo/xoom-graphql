package io.vlingo.graphql.model;

import graphql.schema.DataFetcher;

/**
 *
 */
public class AbstractQueryPart {
    private final String fieldName;
    private final DataFetcher<?> dataFetcher;

    protected AbstractQueryPart(String fieldName, DataFetcher<?> dataFetcher) {
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
