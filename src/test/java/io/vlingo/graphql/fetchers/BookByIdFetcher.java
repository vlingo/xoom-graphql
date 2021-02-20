package io.vlingo.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.vlingo.graphql.Store;
import io.vlingo.graphql.model.Book;

public class BookByIdFetcher implements DataFetcher<Book> {
    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
        String bookId = dataFetchingEnvironment.getArgument("id");
        Book result = Store.books.stream()
                .filter(b -> bookId.equals(b.getId()))
                .findAny()
                .orElse(null);

        return result;
    }
}
