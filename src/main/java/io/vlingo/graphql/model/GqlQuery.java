package io.vlingo.graphql.model;

import graphql.schema.DataFetcher;

import java.util.Arrays;
import java.util.List;

/**
 * GraphQL query.
 */
public class GqlQuery extends GqlAbstractQuery {

    private GqlQuery(String fieldName, DataFetcher<?> dataFetcher) {
        super(fieldName, dataFetcher);
    }

    public static GqlQuery from(final String fieldName, final DataFetcher<?> dataFetcher) {
        return new GqlQuery(fieldName, dataFetcher);
    }

    public static List<GqlQuery> listOf(GqlQuery... queries) {
        return Arrays.asList(queries);
    }
}
