// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.graphql.model.Author;
import io.vlingo.xoom.graphql.model.Book;

public final class Store {
    public static final List<Author> authors = new ArrayList<>(Arrays.asList(
            new Author("author-1", "William", "Shakespeare"),
            new Author("author-2", "Mark", "Twain"),
            new Author("author-3", "Agatha", "Christie")));

    public static final List<Book> books = new ArrayList<>(Arrays.asList(
            new Book("book-1", "Romeo and Juliet", 480, authors.get(0)),
            new Book("book-2", "Murder on the Orient Express", 256, authors.get(2)),
            new Book("book-3", "The Murder of Roger Ackroyd", 312, authors.get(2)),
            new Book("book-4", "The Adventures of Tom Sawyer", 274, authors.get(1)),
            new Book("book-5", "The Adventures of Huckleberry Finn", 366, authors.get(1)),
            new Book("book-6", "Cards on the Table", 288, authors.get(2)),
            new Book("book-7", "Hamlet", 500, authors.get(0))));
}
