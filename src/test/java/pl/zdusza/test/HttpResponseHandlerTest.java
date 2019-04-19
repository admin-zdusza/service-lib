package pl.zdusza.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.zdusza.Statuses;

@RunWith(VertxUnitRunner.class)
public class HttpResponseHandlerTest {

    private static final int PORT = 5040;

    private HttpClient httpClient;

    @Rule
    public RunTestOnContext vertxContext = new RunTestOnContext();

    @Before
    public final void setUp(final TestContext tc) {
        final Handler<AsyncResult<Void>> setUpHandler = tc.asyncAssertSuccess();
        vertxContext.vertx().deployVerticle(new AbstractVerticle() {
            @Override
            public void start(final Future<Void> future) {
                Router router = Router.router(vertx);
                router.get("/testjsonobject")
                        .handler((routingContext) -> routingContext.response().end(new JsonObject()
                                .put("test", "test").toBuffer()));
                router.get("/testvoid")
                        .handler((routingContext) -> routingContext.response().end());
                router.get("/testjsonobjectfail")
                        .handler((routingContext) -> routingContext.response().setStatusCode(Statuses.BAD_REQUEST)
                                .end());
                router.get("/testvoidfail")
                        .handler((routingContext) -> routingContext.response().setStatusCode(Statuses.BAD_REQUEST)
                                .end());
                vertx.createHttpServer().requestHandler(router::accept)
                        .listen(PORT, asyncCall -> {
                            if (asyncCall.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(asyncCall.cause());
                            }
                        });
            }
        }, deployHttpAsyncCall -> {
            if (deployHttpAsyncCall.succeeded()) {
                httpClient = vertxContext.vertx().createHttpClient();
                setUpHandler.handle(Future.succeededFuture());
            } else {
                setUpHandler.handle(Future.failedFuture(deployHttpAsyncCall.cause()));
            }
        });
    }

    @Test
    public final void shouldParseJsonResponse(final TestContext tc) {
        final Async async = tc.async();
        Future<JsonObject> f = Future.future();
        HttpClientRequest request = this.httpClient.get(PORT, "localhost",
                "/testjsonobject", HttpResponseHandler.jsonObject(f));
        request.end();
        f.setHandler(v -> {
            tc.assertTrue(v.succeeded());
            tc.assertEquals(v.result(), new JsonObject().put("test", "test"));
            async.complete();
        });
    }

    @Test
    public final void shouldParseJsonResponseFailWhenResponseFails(final TestContext tc) {
        final Async async = tc.async();
        Future<JsonObject> f = Future.future();
        HttpClientRequest request = this.httpClient.get(PORT, "localhost",
                "/testjsonobjectfail", HttpResponseHandler.jsonObject(f));
        request.end();
        f.setHandler(v -> {
            tc.assertTrue(v.failed());
            async.complete();
        });
    }

    @Test
    public final void shouldHandleVoidResponse(final TestContext tc) {
        final Async async = tc.async();
        Future<Void> f = Future.future();
        HttpClientRequest request = this.httpClient.get(PORT, "localhost",
                "/testvoid", HttpResponseHandler.voidResponse(f));
        request.end();
        f.setHandler(v -> {
            tc.assertTrue(v.succeeded());
            async.complete();
        });
    }

    @Test
    public final void shouldHandleVoidResponseFailWhenResponseFails(final TestContext tc) {
        final Async async = tc.async();
        Future<Void> f = Future.future();
        HttpClientRequest request = this.httpClient.get(PORT, "localhost",
                "/testvoidfail", HttpResponseHandler.voidResponse(f));
        request.end();
        f.setHandler(v -> {
            tc.assertTrue(v.failed());
            async.complete();
        });
    }
}
