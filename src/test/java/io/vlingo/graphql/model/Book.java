package io.vlingo.graphql.model;

public class Book {
    private String id;
    private String name;
    private int pageCount;
    private Author author;

    public Book(String id, String name, int pageCount, Author author) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Author getAuthor() {
        return author;
    }
}
