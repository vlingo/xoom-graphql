package io.vlingo.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorInstantiator;
import io.vlingo.common.Completes;
import io.vlingo.graphql.model.GqlResponse;
import io.vlingo.graphql.model.GqlQuery;
import io.vlingo.graphql.model.GqlType;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class GraphQLProcessorActor extends Actor implements GraphQLProcessor {
    private final GraphQL graphQL;

    public GraphQLProcessorActor(GraphQL graphQL) {
        this.graphQL = graphQL;
    }

    @Override
    public Completes<GqlResponse> query(String query) {
        try {
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .build();

            ExecutionResult executionResult = graphQL.execute(executionInput);

            Object data = executionResult.getData();
            List<GraphQLError> errors = executionResult.getErrors();

            return completes().with(new GqlResponse(data, errors));
        } catch (Exception e) {
            logger().error("Failed to execute query operation.", e);
            return Completes.withFailure();
        }
    }

    @Override
    public Completes<GqlResponse> query(String query, Map<String, Object> variables) {
        try {
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .variables(variables)
                    .build();

            ExecutionResult executionResult = graphQL.execute(executionInput);

            Object data = executionResult.getData();
            List<GraphQLError> errors = executionResult.getErrors();

            return completes().with(new GqlResponse(data, errors));
        } catch (Exception e) {
            logger().error("Failed to execute query operation.", e);
            return Completes.withFailure();
        }
    }

    public static class GraphQLProcessorInstantiator implements ActorInstantiator<GraphQLProcessorActor> {

        private static final String queryKeyword = "Query";

        private final GraphQL graphQL;

        public GraphQLProcessorInstantiator(final String schemaFile, final List<GqlQuery> queries, final List<GqlType> types) {
            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry typeRegistry = schemaParser.parse(
                    new InputStreamReader(GraphQLProcessorActor.class.getResourceAsStream(schemaFile)));

            RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring()
                    .type(queryKeyword, builder -> buildWiring(builder, queries));
            RuntimeWiring wiring = buildTypesWiring(runtimeWiringBuilder, types)
                    .build();

            SchemaGenerator schemaGenerator = new SchemaGenerator();
            GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
            this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        }

        @Override
        public GraphQLProcessorActor instantiate() {
            return new GraphQLProcessorActor(graphQL);
        }

        @Override
        public Class<GraphQLProcessorActor> type() {
            return GraphQLProcessorActor.class;
        }

        private static RuntimeWiring.Builder buildTypesWiring(final RuntimeWiring.Builder builder, final List<GqlType> types) {
            RuntimeWiring.Builder resultBuilder = builder;

            for (GqlType type : types) {
                resultBuilder = resultBuilder.type(type.getTypeName(), tempBuilder -> buildWiring(tempBuilder, type.getFieldQueries()));
            }

            return resultBuilder;
        }

        private static TypeRuntimeWiring.Builder buildWiring(final TypeRuntimeWiring.Builder builder, final List<GqlQuery> queries) {
            TypeRuntimeWiring.Builder resultBuilder = builder;

            for (GqlQuery query : queries) {
                resultBuilder = resultBuilder.dataFetcher(query.getFieldName(), query.getDataFetcher());
            }

            return resultBuilder;
        }
    }
}
