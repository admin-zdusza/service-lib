package pl.zdusza.test;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;
import pl.zdusza.Statuses;

public final class HttpResponseHandler {

    private HttpResponseHandler() {
    }

    public static Handler<HttpClientResponse> jsonObject(final Future<JsonObject> result) {
        return response -> {
            if (response.statusCode() == Statuses.SUCCESS) {
                response.bodyHandler(buffer -> result.complete(new JsonObject(buffer)));
            } else {
                response.bodyHandler(buffer ->
                        result.fail(response.statusCode() + " "
                                + response.statusMessage() + ". "
                                + buffer)
                );
            }
        };
    }

    public static Handler<HttpClientResponse> voidResponse(final Future<Void> result) {
        return response -> {
            if (response.statusCode() == Statuses.SUCCESS) {
                response.bodyHandler(buffer -> result.complete());
            } else {
                response.bodyHandler(buffer ->
                        result.fail(response.statusCode() + " "
                                + response.statusMessage() + ". "
                                + buffer)
                );
            }
        };
    }
}
