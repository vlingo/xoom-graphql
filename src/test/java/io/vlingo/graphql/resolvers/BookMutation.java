package io.vlingo.graphql.resolvers;

import graphql.kickstart.tools.GraphQLMutationResolver;
import io.vlingo.graphql.Store;
import io.vlingo.graphql.model.Book;

public class BookMutation implements GraphQLMutationResolver {
    public Book upsertBook(final String id, final String name, final int pageCount, final String authorId) {
        return Store.authors.stream()
                .filter(a -> a.getId().equals(authorId))
                .map(a -> upsertBook(new Book(id, name, pageCount, a)))
                .findAny()
                .orElse(null);
    }

    private Book upsertBook(final Book aBook) {
        return Store.books.stream()
                .filter(b -> b.getId().equals(aBook.getId()))
                .map(b -> b.updateWith(aBook))
                .findAny()
                .orElseGet(() -> {
                    Store.books.add(aBook);
                    return aBook;
                });
    }
}
