package io.vlingo.graphql;

import io.vlingo.graphql.model.Author;
import io.vlingo.graphql.model.Book;

import java.util.Arrays;
import java.util.List;

public final class Store {
    public static final List<Author> authors = Arrays.asList(
            new Author("author-1", "William", "Shakespeare"),
            new Author("author-2", "Mark", "Twain"),
            new Author("author-3", "Agatha", "Christie"));

    public static final List<Book> books = Arrays.asList(
            new Book("book-1", "Romeo and Juliet", 480, authors.get(0)),
            new Book("book-2", "Murder on the Orient Express", 256, authors.get(2)),
            new Book("book-3", "The Murder of Roger Ackroyd", 312, authors.get(2)),
            new Book("book-4", "The Adventures of Tom Sawyer", 274, authors.get(1)),
            new Book("book-5", "The Adventures of Huckleberry Finn", 366, authors.get(1)),
            new Book("book-6", "Cards on the Table", 288, authors.get(2)),
            new Book("book-7", "Hamlet", 500, authors.get(0)));
}
