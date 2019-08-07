package pl.zdusza;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class HttpsRedirectHandler implements Handler<RoutingContext> {

    private final boolean disable;

    public HttpsRedirectHandler(final boolean disable) {
        this.disable = disable;
    }

    @Override
    public final void handle(final RoutingContext context) {
        if (this.disable || "https".equals(context.request().getHeader("X-Forwarded-Proto"))) {
            context.next();
        } else {
            final String absoluteURI = context.request().absoluteURI();
            final String newLocation = "https" + absoluteURI.substring(absoluteURI.indexOf("://"));
            context.response().putHeader("Location", newLocation)
                    .setStatusCode(Statuses.MOVED_PERMANENTLY).end();
        }
    }
}
