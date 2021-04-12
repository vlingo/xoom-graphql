package io.vlingo.xoom.graphql.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import io.vlingo.xoom.graphql.Store;
import io.vlingo.xoom.graphql.model.Book;

public class BookQuery implements GraphQLQueryResolver {
    public Book getBookById(String bookId) {
        return Store.books.stream()
                .filter(b -> bookId.equals(b.getId()))
                .findAny()
                .orElse(null);
    }
}
