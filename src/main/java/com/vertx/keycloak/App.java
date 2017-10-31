package com.vertx.keycloak;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.OAuth2AuthHandler;


/**
 * Hello world!
 *
 */
public class App extends AbstractVerticle{

        @Override
        public void start(Future<Void> startFuture) throws Exception {

                Router router = Router.router(vertx);

               OAuth2AuthHandler oauth = OAuth2AuthHandler.create(
                       OAuth2Auth.createKeycloak(vertx, OAuth2FlowType.AUTH_CODE, new JsonObject("{\n" +
                               "  \"realm\": \"archer_application\",\n" +
                               "  \"auth-server-url\": \"http://localhost:8080/auth\",\n" +
                               "  \"ssl-required\": \"external\",\n" +
                               "  \"resource\": \"store-services\",\n" +
                               "  \"credentials\": {\n" +
                               "    \"secret\": \"1f2be575-93b7-43a2-a98b-d9c9a04cc7d1\"\n" +
                               "  }\n" +
                               "}")),
                       "http://localhost:8088");
                oauth.setupCallback(router.get("/callback"));

                router.route("/protected/*").handler(oauth);

                router.route("/protected/page").handler(rc->{
                   rc.response().end("Protected Page");
                });
                vertx.createHttpServer().requestHandler(router::accept).listen(8088);
        }

}
