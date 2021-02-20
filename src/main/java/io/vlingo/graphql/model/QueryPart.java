package io.vlingo.graphql.model;

import graphql.schema.DataFetcher;

import java.util.Arrays;
import java.util.List;

/**
 * GraphQL query part.
 */
public class QueryPart extends AbstractQueryPart {

    private QueryPart(String fieldName, DataFetcher<?> dataFetcher) {
        super(fieldName, dataFetcher);
    }

    public static QueryPart from(final String fieldName, final DataFetcher<?> dataFetcher) {
        return new QueryPart(fieldName, dataFetcher);
    }

    public static List<QueryPart> listOf(QueryPart... queries) {
        return Arrays.asList(queries);
    }
}
