// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql.model;

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
