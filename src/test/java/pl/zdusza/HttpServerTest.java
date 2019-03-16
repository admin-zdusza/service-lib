package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class HttpServerTest {

    private static final String PORT = "5029";

    @Rule
    public RunTestOnContext vertxContext = new RunTestOnContext();

    @Test
    public final void testShouldStart(final TestContext tc) {
        Async async = tc.async();
        Vertx vertx = vertxContext.vertx();
        vertx.exceptionHandler(new UnexpectedVertxExceptionHandler());
        Router router = Router.router(vertx);
        router.exceptionHandler(new UnexpectedRouterExceptionHandler());
        router.post().handler(BodyHandler.create());
        Future<Void> f = Future.future();
        HttpServer.start(vertx, f, router, Integer.valueOf(PORT));
        f.setHandler(v -> {
            tc.assertTrue(v.succeeded());
            async.complete();
        });
    }
}
