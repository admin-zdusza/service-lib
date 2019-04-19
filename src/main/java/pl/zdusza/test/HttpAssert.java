package pl.zdusza.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import pl.zdusza.Statuses;

public final class HttpAssert {

    private HttpAssert() {
    }

    public static <T> Handler<AsyncResult<T>> assert200(final TestContext tc,
                                                        final Async async) {
        return v -> {
            if (v.succeeded()) {
                async.complete();
            } else {
                tc.fail(v.cause());
            }
        };
    }

    public static <T> Handler<AsyncResult<T>> assert400(final TestContext tc, final Async async, final String message) {
        return v -> {
            if (v.succeeded()) {
                tc.fail("Test case should fail with 400 but succedded");
            } else {
                tc.assertEquals(v.cause().getMessage(), Statuses.BAD_REQUEST
                        + " " + Statuses.BAD_REQUEST_MSG
                        + ". " + message);
                async.complete();
            }
        };
    }

    public static <T> Handler<AsyncResult<T>> assert401(final TestContext tc, final Async async) {
        return v -> {
            if (v.succeeded()) {
                tc.fail("Test case should fail with 401 but succedded");
            } else {
                tc.assertEquals(v.cause().getMessage(), Statuses.UNAUTHORIZED
                        + " " + Statuses.UNAUTHORIZED_MSG
                        + ". " + Statuses.UNAUTHORIZED_MSG);
                async.complete();
            }
        };
    }

    public static <T> Handler<AsyncResult<T>> assert412(final TestContext tc, final Async async, final String message) {
        return v -> {
            if (v.succeeded()) {
                tc.fail("Test case should fail with 428 but succedded");
            } else {
                tc.assertEquals(v.cause().getMessage(), Statuses.PRECONDITION_FAILED
                        + " " + Statuses.PRECONDITION_FAILED_MSG
                        + ". " + message);
                async.complete();
            }
        };
    }

    public static <T> Handler<AsyncResult<T>> assert500(final TestContext tc,
                                                        final Async async,
                                                        final Runnable runnable) {
        return v -> {
            if (v.succeeded()) {
                tc.fail("Test case should fail with 500 but succedded");
            } else {
                tc.verify(v2 -> {
                    runnable.run();
                    tc.assertEquals(v.cause().getMessage(), Statuses.INTERNAL_SERVER_ERROR
                            + " " + Statuses.INTERNAL_SERVER_ERROR_MSG
                            + ". " + Statuses.INTERNAL_SERVER_ERROR_MSG);
                    async.complete();
                });
            }
        };
    }
}
