package io.vlingo.graphql;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.graphql.model.GqlRequest;
import io.vlingo.http.Response;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class GraphQLResource extends DynamicResourceHandler {
    private final GraphQLProcessor processor;

    public GraphQLResource(final Stage stage, final GraphQLProcessor processor) {
        super(stage);
        this.processor = processor;
    }

    public Completes<Response> graphQLHandler(final GqlRequest request) {
        return processor.query(request.getQuery(), request.getVariables())
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
