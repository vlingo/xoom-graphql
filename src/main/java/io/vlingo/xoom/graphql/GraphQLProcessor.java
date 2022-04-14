// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql;

import java.util.Map;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.graphql.model.GqlResponse;

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
