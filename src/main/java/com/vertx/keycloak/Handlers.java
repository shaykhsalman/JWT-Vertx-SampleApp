package com.vertx.keycloak;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

public class Handlers {
    private final List<Book> books = new ArrayList<>();

    public Handlers() {
        books.add(new Book("Java 9 modularity", "java", "modularity", "jigsaw"));
        books.add(new Book("Building Cloud Apps with OSGi", "java", "modularity", "osgi"));
    }

    public void listBooks(RoutingContext rc) {
        rc.response().end(Json.encode(books));
    }

    public void userLogger(RoutingContext rc) {
        User user = rc.user();
        if (user != null) {
            JsonObject principal = user.principal();
            System.out.println("Request by {}" + principal.getString("preferred_username"));
        } else {
            System.out.println("No logged in user");
        }

        rc.next();
    }

}
