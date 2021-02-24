package io.vlingo.graphql;

import io.vlingo.common.Completes;
import io.vlingo.graphql.model.GqlResponse;

import java.util.Map;

/**
 * Processor class that executes GraphQL operations.
 */
public interface GraphQLProcessor {
    /**
     * Executes GraphQL query (i.e Query, Mutation, ...) operation.
     *
     * @param query Query operation to execute
     * @param variables Variables for query operation
     * @return
     */
    Completes<GqlResponse> query(String query, Map<String, Object> variables);
}
