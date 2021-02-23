package io.vlingo.graphql.model;

import java.util.Arrays;
import java.util.List;

/**
 * GraphQL type.
 */
public class GqlType {
    private final String typeName;
    private final List<GqlQuery> fieldQueries;

    private GqlType(final String typeName, final List<GqlQuery> fieldQueries) {
        this.typeName = typeName;
        this.fieldQueries = fieldQueries;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<GqlQuery> getFieldQueries() {
        return fieldQueries;
    }

    public static GqlType from(final String typeName, final List<GqlQuery> fieldQueries) {
        return new GqlType(typeName, fieldQueries);
    }

    public static List<GqlType> listOf(GqlType... types) {
        return Arrays.asList(types);
    }
}
