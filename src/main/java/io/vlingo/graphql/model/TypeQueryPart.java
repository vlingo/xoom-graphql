package io.vlingo.graphql.model;

import graphql.schema.DataFetcher;

import java.util.Arrays;
import java.util.List;

/**
 * GraphQL type query part.
 */
public class TypeQueryPart extends AbstractQueryPart {

    private TypeQueryPart(String fieldName, DataFetcher<?> dataFetcher) {
        super(fieldName, dataFetcher);
    }

    public static TypeQueryPart from(final String fieldName, final DataFetcher<?> dataFetcher) {
        return new TypeQueryPart(fieldName, dataFetcher);
    }

    public static List<TypeQueryPart> listOf(TypeQueryPart... queries) {
        return Arrays.asList(queries);
    }
}
