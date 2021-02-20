package io.vlingo.graphql;

import io.vlingo.common.Completes;
import io.vlingo.graphql.model.GraphQLResponse;

import java.util.Map;

/**
 * Processor class that executes GraphQL operations.
 */
public interface GraphQLProcessor {
    /**
     * Executes GraphQL query operation.
     *
     * @param query Query operation to execute
     * @return
     */
    Completes<GraphQLResponse> query(String query);

    /**
     * Executes GraphQL query operation.
     *
     * @param query Query operation to execute
     * @param variables Variables for query operation
     * @return
     */
    Completes<GraphQLResponse> query(String query, Map<String, Object> variables);
}
