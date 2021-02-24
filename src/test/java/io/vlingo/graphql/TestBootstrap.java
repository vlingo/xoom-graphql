package io.vlingo.graphql;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.graphql.resolvers.BookMutation;
import io.vlingo.graphql.resolvers.BookQuery;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

import java.util.Arrays;

/**
 * Convenient test bootstrap to run vlingo-graphql utilizing test resources.
 */
public class TestBootstrap {
    private static TestBootstrap instance = null;

    private final World world;
    private final Server server;

    public TestBootstrap(final int port) {
        world = World.startWithDefaults("test-vlingo-graphql");
        final GraphQLProcessor processor = newProcessor(world.stage());
        final GraphQLResource graphQLResource = new GraphQLResource(world.stage(), processor);
        final Resources allResources = Resources.are(graphQLResource.routes());

        server = Server.startWith(world.stage(), allResources, port, Configuration.Sizing.define(), new Configuration.Timing(4L, 2L, 100L));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();
                System.out.println("=========================");
                System.out.println("Stopping test-vlingo-graphql");
                System.out.println("=========================");
            }
        }));
    }

    private GraphQLProcessor newProcessor(final Stage stage) {
        final GraphQLProcessorActor.GraphQLProcessorInstantiator instantiator = new GraphQLProcessorActor.GraphQLProcessorInstantiator(
                "schema.graphqls", Arrays.asList(new BookQuery(), new BookMutation()));

        final Definition definition = Definition.has(GraphQLProcessorActor.class, instantiator);
        final GraphQLProcessor processor = stage.actorFor(GraphQLProcessor.class, definition);

        return processor;
    }

    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("Starting: test-vlingo-graphql");
        System.out.println("=========================");

        instance = new TestBootstrap(18085);
    }


}
