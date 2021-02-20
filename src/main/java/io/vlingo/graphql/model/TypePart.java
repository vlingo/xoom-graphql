package io.vlingo.graphql.model;

import java.util.Arrays;
import java.util.List;

/**
 * GraphQL type part.
 */
public class TypePart {
    private final String typeName;
    private final List<QueryPart> fieldQueries;

    private TypePart(final String typeName, final List<QueryPart> fieldQueries) {
        this.typeName = typeName;
        this.fieldQueries = fieldQueries;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<QueryPart> getFieldQueries() {
        return fieldQueries;
    }

    public static TypePart from(final String typeName, final List<QueryPart> fieldQueries) {
        return new TypePart(typeName, fieldQueries);
    }

    public static List<TypePart> listOf(TypePart... types) {
        return Arrays.asList(types);
    }
}
