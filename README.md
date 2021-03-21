# vlingo-graphql

The VLINGO XOOM platform SDK GraphQL Server running on XOOM HTTP.

# PROPRIETARY AND CONFIDENTIAL

# CLOSED SOURCE ACCESSIBLE BY INVITATION ONLY

# References

https://graphql.org/

https://graphql.org/learn/serving-over-http

https://www.graphql-java.com/

# Code Example: Initializing GraphQL

```
public GraphQL graphQL(final String schemaName, final WarbleDataFetcher warbleDataFetcher) {

    final SchemaParser schemaParser = new SchemaParser();
    final SchemaGenerator schemaGenerator = new SchemaGenerator();

    // Parse schema
    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    
    // SchemaLoader is our component (yet to be implemented) that loads
    // a GraphQL schema as a String
    final String schema = SchemaLoader.by(schemaName);
    
    typeRegistry.merge(schemaParser.parse(schema));

    // New runtime wiring
    final RuntimeWiring runtimeWiring =
            RuntimeWiring.newRuntimeWiring()
                    .type("Query", typeWiring -> typeWiring.dataFetcher("warble", warbleDataFetcher))
                    .build();

    // New executable schema
    final GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

    // Answer GraphQL instance
    return GraphQL.newGraphQL(graphQLSchema).build();
}
```

### Important
If using snapshot builds [follow these instructions](https://github.com/vlingo/vlingo-platform#snapshots-repository) or you will experience failures.

