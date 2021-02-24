package io.vlingo.graphql.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import io.vlingo.graphql.Store;
import io.vlingo.graphql.model.Book;

public class BookQuery implements GraphQLQueryResolver {
    public Book getBookById(String bookId) {
        return Store.books.stream()
                .filter(b -> bookId.equals(b.getId()))
                .findAny()
                .orElse(null);
    }
}
