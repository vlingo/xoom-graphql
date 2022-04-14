# xoom-graphql

[![Javadocs](http://javadoc.io/badge/io.vlingo.xoom/xoom-graphql.svg?color=brightgreen)](http://javadoc.io/doc/io.vlingo.xoom/xoom-graphql) [![Build](https://github.com/vlingo/xoom-graphql/workflows/Build/badge.svg)](https://github.com/vlingo/xoom-graphql/actions?query=workflow%3ABuild) [![Download](https://img.shields.io/maven-central/v/io.vlingo.xoom/xoom-graphql?label=maven)](https://search.maven.org/artifact/io.vlingo.xoom/xoom-graphql) [![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/http)

The VLINGO XOOM platform SDK GraphQL Server running on XOOM HTTP.

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
If using snapshot builds [follow these instructions](https://github.com/vlingo/xoom-platform#snapshots-repository) or you will experience failures.


License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2022 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
