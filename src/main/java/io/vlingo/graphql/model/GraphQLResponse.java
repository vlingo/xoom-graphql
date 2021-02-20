package io.vlingo.graphql.model;

import graphql.GraphQLError;

import java.util.List;

/**
 * GraphQL response model.
 */
public class GraphQLResponse {
    private final Object data;
    private final List<GraphQLError> errors;

    public GraphQLResponse(Object data, List<GraphQLError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public List<GraphQLError> getErrors() {
        return errors;
    }
}
