package io.vlingo.graphql;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.graphql.fetchers.BookByIdFetcher;
import io.vlingo.graphql.model.QueryPart;
import io.vlingo.graphql.model.TypePart;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GraphQLTest {
    private static World world;

    @Test
    public void parseSchemaTest() {
        final GraphQLProcessorActor.GraphQLProcessorInstantiator instantiator = new GraphQLProcessorActor.GraphQLProcessorInstantiator(
                "/schema.graphqls",
                QueryPart.listOf(
                        QueryPart.from("bookById", new BookByIdFetcher())),
                TypePart.listOf(
                        TypePart.from("Book", new ArrayList<>()),
                        TypePart.from("Author", new ArrayList<>())));

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

        processor.query(query);
    }

    @BeforeAll
    public static void setUp() {
        world = World.startWithDefaults("vlingo-graphql-test");
    }

    @AfterAll
    public static void tearDown() {
        world.stage().stop();
    }
}