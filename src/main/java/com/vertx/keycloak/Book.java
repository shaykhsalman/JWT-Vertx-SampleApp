package com.vertx.keycloak;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Book {
    private final String title;
    private final Set<String> tags;

    public Book(String title, String... tags) {
        this.title = title;
        this.tags = new HashSet<>();
        this.tags.addAll(Arrays.asList(tags));
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getTags() {
        return tags;
    }
}
