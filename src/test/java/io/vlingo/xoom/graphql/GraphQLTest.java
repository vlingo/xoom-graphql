package io.vlingo.xoom.graphql;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.graphql.model.GqlResponse;
import io.vlingo.xoom.graphql.resolvers.BookMutation;
import io.vlingo.xoom.graphql.resolvers.BookQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

public class GraphQLTest {
    private static World world;

    @Test
    public void parseSchemaTest() {
        final GraphQLProcessorActor.GraphQLProcessorInstantiator instantiator = new GraphQLProcessorActor.GraphQLProcessorInstantiator(
                "schema.graphqls", Arrays.asList(new BookQuery(), new BookMutation()));

        final Definition definition = Definition.has(GraphQLProcessorActor.class, instantiator);
        GraphQLProcessor processor = world.stage().actorFor(GraphQLProcessor.class, definition);

        final String query = "query {\n" +
                "    bookById(id: \"book-1\") {\n" +
                "        id\n" +
                "        name\n" +
                "        pageCount\n" +
                "        author {\n" +
                "            firstName\n" +
                "            lastName\n" +
                "        }\n" +
                "    }\n" +
                "}";

        final GqlResponse queryResponse = processor.query(query, new HashMap<>()).await();
        Assertions.assertEquals(0, queryResponse.errors.size());

        final String mutation = "mutation {\n" +
                "    upsertBook(id: \"book-4\", name: \"The Mysterious Stranger\", pageCount: 176, authorId: \"author-2\") {\n" +
                "        id\n" +
                "        name\n" +
                "        pageCount\n" +
                "        author {\n" +
                "            firstName\n" +
                "            lastName\n" +
                "        }\n" +
                "    }\n" +
                "}";
        final GqlResponse mutationResponse = processor.query(mutation, new HashMap<>()).await();
        Assertions.assertEquals(0, mutationResponse.errors.size());
    }

    @BeforeAll
    public static void setUp() {
        world = World.startWithDefaults("xoom-graphql-test");
    }

    @AfterAll
    public static void tearDown() {
        world.stage().stop();
    }
}