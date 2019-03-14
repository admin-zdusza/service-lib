package pl.zdusza;

import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zdusza.event.LoggableEventName;

public class UnexpectedRouterExceptionHandler implements Handler<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnexpectedRouterExceptionHandler.class);

    @Override
    public final void handle(final Throwable throwable) {
        LOGGER.error(LoggableEventName.UNHANDLED_ROUTER_EXCEPTION.name(),
                throwable);
    }
}
