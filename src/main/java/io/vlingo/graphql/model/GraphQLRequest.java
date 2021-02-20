package io.vlingo.graphql.model;

import java.util.HashMap;
import java.util.Map;

/**
 * GraphQL request model.
 */
public class GraphQLRequest {
    private String query;
    private Map<String, Object> variables = new HashMap<>();
    private String operationName;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
