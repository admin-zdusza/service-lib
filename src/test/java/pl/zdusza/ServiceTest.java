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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zdusza.time.Clock;
import pl.zdusza.time.Timer;

public class ServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTest.class);

    private static final RuntimeException TEST_EXCEPTION = new RuntimeException("Test exception 1");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoutingContext routingContext;

    @Mock
    private Message<String> message;

    @Test
    public final void testShouldRCSucced() {
        Service.doInTryCatch(Future.succeededFuture(), routingContext, () -> {
        }, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(routingContext, Mockito.times(0)).fail(TEST_EXCEPTION);
    }


    @Test
    public final void testShouldRCFailWhenThrowingException() {
        Service.doInTryCatch(Future.succeededFuture(), routingContext, () -> {
            throw TEST_EXCEPTION;
        }, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(routingContext).fail(TEST_EXCEPTION);
    }

    @Test
    public final void testShouldRCFailForFailingFuture() {
        Service.doInTryCatch(Future.failedFuture(TEST_EXCEPTION), routingContext, () -> {
        }, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(routingContext).fail(TEST_EXCEPTION);
    }

    @Test
    public final void testShouldMSGSucced() {
        Service.doInTryCatch(Future.succeededFuture(), message, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(message, Mockito.times(0)).fail(Mockito.anyInt(), Mockito.any());
    }

    @Test
    public final void testShouldMSGFailWhenThrowingException() {
        Mockito.doThrow(TEST_EXCEPTION).when(message).reply(Mockito.any());
        Mockito.doNothing().when(message).fail(Mockito.anyInt(), Mockito.any());
        Service.doInTryCatch(Future.succeededFuture(), message, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(message).fail(Mockito.anyInt(), Mockito.any());
    }

    @Test
    public final void testShouldMSGFailWhenFailing() {
        Mockito.doNothing().when(message).fail(Mockito.anyInt(), Mockito.any());
        Service.doInTryCatch(Future.failedFuture(TEST_EXCEPTION), message, new Timer(new Clock(), "", LOGGER));
        Mockito.verify(message).fail(Mockito.anyInt(), Mockito.any());
    }
}
