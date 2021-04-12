package io.vlingo.xoom.graphql.model;

import graphql.GraphQLError;

import java.util.List;

/**
 * GraphQL response model.
 */
public class GqlResponse {
    public final Object data;
    public final List<GraphQLError> errors;

    public GqlResponse(Object data, List<GraphQLError> errors) {
        this.data = data;
        this.errors = errors;
    }
}
