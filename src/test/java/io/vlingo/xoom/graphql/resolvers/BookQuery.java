// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

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
