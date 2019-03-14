package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    private HttpServer() {
    }

    public static void start(final Vertx vertx,
                             final Future<Void> future,
                             final Router router,
                             final int port) {
        vertx.createHttpServer().requestHandler(router::accept)
                .listen(port, asyncCall -> {
                    if (asyncCall.succeeded()) {
                        LOGGER.info("HTTP server running on port {}", port);
                        future.complete();
                    } else {
                        future.fail(asyncCall.cause());
                    }
                });
    }
}
