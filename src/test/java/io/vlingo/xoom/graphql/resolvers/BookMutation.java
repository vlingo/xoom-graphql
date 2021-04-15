// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.graphql.resolvers;

import graphql.kickstart.tools.GraphQLMutationResolver;
import io.vlingo.xoom.graphql.Store;
import io.vlingo.xoom.graphql.model.Book;

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
