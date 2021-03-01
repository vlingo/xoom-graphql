package io.vlingo.graphql.model;

import java.util.HashMap;
import java.util.Map;

/**
 * GraphQL request model.
 */
public class GqlRequest {
    public final String query;
    public final Map<String, Object> variables;
    public final String operationName;

    public GqlRequest(String query, Map<String, Object> variables, String operationName) {
        this.query = query;
        this.variables = variables;
        this.operationName = operationName;
    }
}
