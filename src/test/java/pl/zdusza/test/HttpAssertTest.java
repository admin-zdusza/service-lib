package pl.zdusza.test;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(VertxUnitRunner.class)
public class HttpAssertTest {

    @Mock
    private TestContext testContext;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public final void assert200ShouldFailOnFail(final TestContext tc) {
        Async async = tc.async();
        Future.failedFuture("Test").setHandler(event -> {
            HttpAssert.assert200(testContext, async).handle(event);
            Mockito.verify(testContext, Mockito.times(1)).fail(Mockito.<Throwable>any());
            async.complete();
        });
    }

    @Test
    public final void assert200ShouldNotFailOnSuccess(final TestContext tc) {
        Async async = tc.async();
        Future.succeededFuture()
                .setHandler(HttpAssert.assert200(tc, async));
    }

    @Test
    public final void assert400ShouldFailOnSuccess(final TestContext tc) {
        Async async = tc.async();
        Future.succeededFuture().setHandler(event -> {
            HttpAssert.assert400(testContext, async, "Test").handle(event);
            Mockito.verify(testContext, Mockito.times(1)).fail(Mockito.anyString());
            async.complete();
        });
    }

    @Test
    public final void assert400ShouldNotFailOnFailure(final TestContext tc) {
        Async async = tc.async();
        Future.failedFuture("400 Bad Request. Test")
                .setHandler(HttpAssert.assert400(tc, async, "Test"));
    }

    @Test
    public final void assert401ShouldFailOnSuccess(final TestContext tc) {
        Async async = tc.async();
        Future.succeededFuture().setHandler(event -> {
            HttpAssert.assert401(testContext, async).handle(event);
            Mockito.verify(testContext, Mockito.times(1)).fail(Mockito.anyString());
            async.complete();
        });
    }

    @Test
    public final void assert401ShouldNotFailOnFailure(final TestContext tc) {
        Async async = tc.async();
        Future.failedFuture("401 Unauthorized. Unauthorized")
                .setHandler(HttpAssert.assert401(tc, async));
    }

    @Test
    public final void assert428ShouldFailOnSuccess(final TestContext tc) {
        Async async = tc.async();
        Future.succeededFuture().setHandler(event -> {
            HttpAssert.assert412(testContext, async, "Test").handle(event);
            Mockito.verify(testContext, Mockito.times(1)).fail(Mockito.anyString());
            async.complete();
        });
    }

    @Test
    public final void assert428ShouldNotFailOnFailure(final TestContext tc) {
        Async async = tc.async();
        Future.failedFuture("412 Precondition Failed. Test")
                .setHandler(HttpAssert.assert412(tc, async, "Test"));
    }

    @Test
    public final void assert500ShouldFailOnSuccess(final TestContext tc) {
        Async async = tc.async();
        Future.succeededFuture().setHandler(event -> {
            HttpAssert.assert500(testContext, async, () -> {
            }).handle(event);
            Mockito.verify(testContext, Mockito.times(1)).fail(Mockito.anyString());
            async.complete();
        });
    }

    @Test
    public final void assert500ShouldNotFailOnFailure(final TestContext tc) {
        Async async = tc.async();
        Future.failedFuture("500 Internal Server Error. Internal Server Error")
                .setHandler(HttpAssert.assert500(tc, async, () -> {
                }));
    }
}
