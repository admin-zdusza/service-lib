package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zdusza.time.Timer;

public final class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private Service() {
    }

    public static <T> void doInTryCatch(final Future<T> future, final RoutingContext routingContext, final Runnable r,
                                        final Timer timer) {
        timer.start();
        future.setHandler(ac -> {
            if (ac.succeeded()) {
                try {
                    r.run();
                    timer.stop();
                } catch (Throwable t) {
                    routingContext.fail(t);
                }
            } else {
                routingContext.fail(ac.cause());
            }
        });
    }

    public static <T> void doInTryCatch(final Future<T> future, final Message<String> message,
                                        final Timer timer) {
        timer.start();
        future.setHandler(ac -> {
            if (future.succeeded()) {
                try {
                    message.reply(Statuses.SUCCESS);
                    timer.stop();
                } catch (Throwable t) {
                    LOGGER.error("Unexpected error occured:", t);
                    message.fail(Statuses.INTERNAL_SERVER_ERROR, Statuses.INTERNAL_SERVER_ERROR_MSG);
                }
            } else {
                LOGGER.error("Unexpected error occured:", future.cause());
                message.fail(Statuses.INTERNAL_SERVER_ERROR, Statuses.INTERNAL_SERVER_ERROR_MSG);
            }
        });
    }
}
