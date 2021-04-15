// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.ContentType;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import java.util.HashMap;
import java.util.Optional;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.graphql.model.GqlRequest;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

public class GraphQLResource extends DynamicResourceHandler {
    private final GraphQLProcessor processor;

    public GraphQLResource(final Stage stage, final GraphQLProcessor processor) {
        super(stage);
        this.processor = processor;
    }

    public Completes<Response> graphQLHandler(final GqlRequest request) {
        return processor.query(request.query, Optional.ofNullable(request.variables).orElse(new HashMap<>()))
                .andThenTo(resp -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(resp))))
                .otherwise(noData -> Response.of(Response.Status.InternalServerError));
    }

    @Override
    public Resource<?> routes() {
        return resource("GraphQL Resource",
                post("/graphql")
                        .body(GqlRequest.class)
                        .handle(this::graphQLHandler));
    }
}
