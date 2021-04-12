package io.vlingo.xoom.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.graphql.model.GqlResponse;

import java.util.List;
import java.util.Map;

public class GraphQLProcessorActor extends Actor implements GraphQLProcessor {
    private final GraphQLSchema graphQLSchema;

    public GraphQLProcessorActor(final GraphQLSchema graphQLSchema) {
        this.graphQLSchema = graphQLSchema;
    }

    @Override
    public Completes<GqlResponse> query(String query, Map<String, Object> variables) {
        GraphQL gql = GraphQL.newGraphQL(graphQLSchema).build();

        try {
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .variables(variables)
                    .build();

            ExecutionResult executionResult = gql.execute(executionInput);

            Object data = executionResult.getData();
            List<GraphQLError> errors = executionResult.getErrors();

            return completes().with(new GqlResponse(data, errors));
        } catch (Exception e) {
            logger().error("Failed to execute query operation.", e);
            return Completes.withFailure();
        }
    }

    public static class GraphQLProcessorInstantiator implements ActorInstantiator<GraphQLProcessorActor> {

        private final GraphQLSchema graphQLSchema;

        public GraphQLProcessorInstantiator(final String schemaFile, final List<GraphQLResolver<?>> resolvers) {
            this.graphQLSchema = SchemaParser.newParser()
                    .file(schemaFile)
                    .resolvers(resolvers)
                    .build()
                    .makeExecutableSchema();
        }

        @Override
        public GraphQLProcessorActor instantiate() {
            return new GraphQLProcessorActor(graphQLSchema);
        }

        @Override
        public Class<GraphQLProcessorActor> type() {
            return GraphQLProcessorActor.class;
        }
    }
}
