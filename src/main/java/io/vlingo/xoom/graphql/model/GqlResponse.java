// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql.model;

import java.util.List;

import graphql.GraphQLError;

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
