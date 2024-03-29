// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql.integration;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.graphql.GraphQLProcessor;
import io.vlingo.xoom.graphql.GraphQLResource;
import io.vlingo.xoom.graphql.TestBootstrap;
import io.vlingo.xoom.graphql.client.model.*;
import io.vlingo.xoom.http.Body;
import io.vlingo.xoom.http.Method;
import io.vlingo.xoom.http.Request;
import io.vlingo.xoom.http.RequestHeader;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.Client;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.ResponseConsumer;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Host;

/**
 * Book store integration test. It is a show case of a GraphQL client.
 */
public class BookStoreTest {
    private static final String host = "localhost";
    private static final int port = 18085;

    private static World world;
    private static Server server;
    private static Client client;

    @Test
    public void queryExistingBookTest() {
        final BookByIdQueryRequest bookRequest = BookByIdQueryRequest.builder()
                .setId("book-1")
                .build();

        final AuthorResponseProjection authorResponseProjection = new AuthorResponseProjection()
                .firstName()
                .lastName();

        final BookResponseProjection bookResponseProjection = new BookResponseProjection()
                .id()
                .name()
                .pageCount()
                .author(authorResponseProjection);

        Response response = performRequest(bookRequest, bookResponseProjection);
        Assertions.assertEquals("200", response.statusCode);

        BookByIdQueryResponse queryResponse = JsonSerialization.deserialized(response.entity.content(), BookByIdQueryResponse.class);
        Assertions.assertEquals(0, queryResponse.getErrors().size());

        Book book = queryResponse.bookById();
        Assertions.assertEquals("book-1", book.getId());
        Assertions.assertEquals("Romeo and Juliet", book.getName());
        Assertions.assertEquals("William", book.getAuthor().getFirstName());
        Assertions.assertEquals("Shakespeare", book.getAuthor().getLastName());
    }

    @Test
    public void upsertBookTest() {
        final String newBookId = "book-8";
        final String newBookName = "The Mysterious Stranger";
        final int newBookPageCount = 200;
        final String newBookAuthorId = "author-2";

        final BookByIdQueryRequest bookRequest = BookByIdQueryRequest.builder()
                .setId(newBookId)
                .build();

        // lookup new book in the store
        final BookResponseProjection simpleBookResponseProjection = new BookResponseProjection()
                .id()
                .name();

        Response queryResponse = performRequest(bookRequest, simpleBookResponseProjection);
        Assertions.assertEquals("200", queryResponse.statusCode);

        BookByIdQueryResponse bookByIdQueryResponse = JsonSerialization.deserialized(queryResponse.entity.content(), BookByIdQueryResponse.class);
        Assertions.assertEquals(0, bookByIdQueryResponse.getErrors().size());

        Book notFound = bookByIdQueryResponse.bookById();
        // new book is not found in the store
        Assertions.assertNull(notFound);

        // insert new book in the store
        final UpsertBookMutationRequest mutationRequest = UpsertBookMutationRequest.builder()
                .setId(newBookId)
                .setName(newBookName)
                .setPageCount(newBookPageCount)
                .setAuthorId(newBookAuthorId)
                .build();

        final AuthorResponseProjection authorResponseProjection = new AuthorResponseProjection()
                .id()
                .firstName()
                .lastName();

        final BookResponseProjection bookResponseProjection = new BookResponseProjection()
                .id()
                .name()
                .pageCount()
                .author(authorResponseProjection);

        Response mutationResponse = performRequest(mutationRequest, bookResponseProjection);
        Assertions.assertEquals("200", mutationResponse.statusCode);

        UpsertBookMutationResponse upsertBookMutationResponse = JsonSerialization.deserialized(mutationResponse.entity.content(), UpsertBookMutationResponse.class);
        Assertions.assertEquals(0, upsertBookMutationResponse.getErrors().size());

        Book newBook = upsertBookMutationResponse.upsertBook();
        Assertions.assertEquals(newBookId, newBook.getId());
        Assertions.assertEquals(newBookName, newBook.getName());
        Assertions.assertEquals(newBookPageCount, newBook.getPageCount());
        Assertions.assertEquals(newBookAuthorId, newBook.getAuthor().getId());
        Assertions.assertEquals("Mark", newBook.getAuthor().getFirstName());
        Assertions.assertEquals("Twain", newBook.getAuthor().getLastName());
    }

    @BeforeAll
    public static void setUp() throws Exception {
        world = World.startWithDefaults("xoom-graphql-test");

        GraphQLProcessor processor = TestBootstrap.newProcessor(world.stage());
        GraphQLResource graphQLResource = new GraphQLResource(world.stage(), processor);

        server = Server.startWith(world.stage(), Resources.are(graphQLResource.routes()), port, Configuration.Sizing.define(),
                new Configuration.Timing(4L, 2L, 100L));

        final Address address = Address.from(Host.of(host), port, AddressType.NONE);
        client = Client.using(Client.Configuration.defaultedKeepAliveExceptFor(world.stage(), address, new UnknownResponseConsumer()));
    }

    @AfterAll
    public static void tearDown() {
        if (client != null) client.close();
        if (server != null) server.stop();

        world.stage().stop();
        world.terminate();
    }

    @SuppressWarnings("unused")
    private Response performRequest(final GraphQLOperationRequest request, final GraphQLResponseProjection responseProjection) {
        final GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        final String graphQLQuery = graphQLRequest.toHttpJsonBody();

        final AccessResponseConsumer responseConsumer = new AccessResponseConsumer();

        client.requestWith(
                Request
                        .has(Method.POST)
                        .and(URI.create("/graphql"))
                        .and(RequestHeader.contentLength(graphQLQuery))
                        .and(RequestHeader.keepAlive())
                        .and(Body.from(graphQLQuery)))
                .andFinallyConsume(responseConsumer::consume);

        return responseConsumer.accessResponse();
    }

    private static class UnknownResponseConsumer implements ResponseConsumer {
        @Override
        public void consume(Response response) {
            System.out.println("Unknown response received: " + response);
        }
    }

    private static class AccessResponseConsumer implements ResponseConsumer {
        private static final String responseAccessName = "response";

        private final AccessSafely access = AccessSafely.afterCompleting(1);

        public AccessResponseConsumer() {
            AtomicReference<Response> responseHolder = new AtomicReference<>();
            access.writingWith(responseAccessName, responseHolder::set);
            access.readingWith(responseAccessName, responseHolder::get);
        }

        @Override
        public void consume(Response response) {
            access.writeUsing(responseAccessName, response);
        }

        public Response accessResponse() {
            return access.readFrom(responseAccessName);
        }
    }
}
