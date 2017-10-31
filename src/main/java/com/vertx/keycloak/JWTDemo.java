package com.vertx.keycloak;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class JWTDemo extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        JsonObject authConfig = new JsonObject().put("keyStore", new JsonObject()
                .put("type", "jceks")
                .put("path", "keystore.jceks")
                .put("password", "secret"));

        JWTAuth jwt = JWTAuth.create(vertx, authConfig);
        router.route("/api/*").handler(JWTAuthHandler.create(jwt, "/api/newToken"));

        // this route is excluded from the auth handler
        router.get("/api/newToken").handler(ctx -> {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end(jwt.generateToken(new JsonObject().put("username", "salman"), new JWTOptions()));
        });

        // this is the secret API
        router.get("/api/protected").handler(ctx -> {
            String data = ctx.user().principal().getString("username");
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end("a secret you should keep for yourself..." +data);
        });

        // Serve the non private static pages
        router.route().handler(StaticHandler.create());

        vertx.createHttpServer().requestHandler(router::accept).listen(8082);
    }
}