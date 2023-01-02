// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql;

import java.util.Arrays;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.graphql.resolvers.BookMutation;
import io.vlingo.xoom.graphql.resolvers.BookQuery;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;

/**
 * Convenient test bootstrap to run xoom-graphql utilizing test resources.
 */
public class TestBootstrap {
    private static TestBootstrap instance = null;

    private final World world;
    private final Server server;

    public TestBootstrap(final int port) {
        world = World.startWithDefaults("test-xoom-graphql");
        final GraphQLProcessor processor = newProcessor(world.stage());
        final GraphQLResource graphQLResource = new GraphQLResource(world.stage(), processor);
        final Resources allResources = Resources.are(graphQLResource.routes());

        server = Server.startWith(world.stage(), allResources, port, Configuration.Sizing.define(), new Configuration.Timing(4L, 2L, 100L));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();
                System.out.println("=========================");
                System.out.println("Stopping test-xoom-graphql");
                System.out.println("=========================");
            }
        }));
    }

    public static GraphQLProcessor newProcessor(final Stage stage) {
        final GraphQLProcessorActor.GraphQLProcessorInstantiator instantiator = new GraphQLProcessorActor.GraphQLProcessorInstantiator(
                "schema.graphqls", Arrays.asList(new BookQuery(), new BookMutation()));

        final Definition definition = Definition.has(GraphQLProcessorActor.class, instantiator);
        final GraphQLProcessor processor = stage.actorFor(GraphQLProcessor.class, definition);

        return processor;
    }

    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("Starting: test-xoom-graphql");
        System.out.println("=========================");

        instance = new TestBootstrap(18085);
    }


}
