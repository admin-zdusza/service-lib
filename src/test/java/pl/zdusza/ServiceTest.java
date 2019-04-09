package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.zdusza.event.EventLogger;
import pl.zdusza.time.Clock;
import pl.zdusza.time.Timer;

public class ServiceTest {

    private static final RuntimeException TEST_EXCEPTION = new RuntimeException("Test exception 1");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoutingContext routingContext;

    @Mock
    private Message<String> message;

    @Mock
    private EventLogger eventLogger;

    @Test
    public final void testShouldRCSucced() {
        Service.doInTryCatch(Future.succeededFuture(), routingContext, () -> {
            routingContext.response();
        }, new Timer(new Clock(), "", eventLogger));
        Mockito.verify(routingContext).response();
    }
    
    @Test
    public final void testShouldRCFailWhenThrowingException() {
        Service.doInTryCatch(Future.succeededFuture(), routingContext, () -> {
            throw TEST_EXCEPTION;
        }, new Timer(new Clock(), "", eventLogger));
        Mockito.verify(routingContext).fail(TEST_EXCEPTION);
    }

    @Test
    public final void testShouldRCFailForFailingFuture() {
        Service.doInTryCatch(Future.failedFuture(TEST_EXCEPTION), routingContext, () -> {
        }, new Timer(new Clock(), "", eventLogger));
        Mockito.verify(routingContext).fail(TEST_EXCEPTION);
    }

    @Test
    public final void testShouldMSGSucced() {
        Service.doInTryCatch(Future.succeededFuture(), message, new Timer(new Clock(), "", eventLogger));
        Mockito.verify(message).reply(Mockito.any());
    }

    @Test
    public final void testShouldMSGFailWhenThrowingException() {
        Mockito.doThrow(TEST_EXCEPTION).when(message).reply(Mockito.any());
        Service.doInTryCatch(Future.succeededFuture(), message, new Timer(new Clock(), "", eventLogger));
        Mockito.verify(message).fail(Mockito.anyInt(), Mockito.any());
    }

    @Test
    public final void testShouldMSGFailWhenFailing() {
        Service.doInTryCatch(Future.failedFuture(TEST_EXCEPTION), message, new Timer(new Clock(), "",
                eventLogger));
        Mockito.verify(message).fail(Mockito.anyInt(), Mockito.any());
    }
}
