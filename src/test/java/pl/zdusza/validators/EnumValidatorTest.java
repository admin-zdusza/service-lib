package pl.zdusza.validators;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.zdusza.App;

public class EnumValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoutingContext routingContext;

    @Mock
    private HttpServerRequest httpServerRequest;

    @Mock
    private HttpServerResponse httpServerResponse;

    @Test
    public final void shouldValidateHeaderEnum() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getHeader("ws-app")).thenReturn("ACC");
        Assert.assertEquals(App.ACC, EnumValidator.validateHeader(routingContext, "ws-app", App.values(),
                App.class).get());
    }

    @Test
    public final void shouldFailToValidateHeaderEnumOnMissingHeader() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getHeader("ws-app")).thenReturn(null);
        Mockito.when(routingContext.response()).thenReturn(httpServerResponse);
        Mockito.when(httpServerResponse.setStatusCode(Mockito.anyInt())).thenReturn(httpServerResponse);
        Assert.assertFalse(EnumValidator.validateHeader(routingContext, "ws-app", App.values(),
                App.class).isPresent());
    }

    @Test
    public final void shouldFailToValidateHeaderEnumOnEmptyHeader() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getHeader("ws-app")).thenReturn("");
        Mockito.when(routingContext.response()).thenReturn(httpServerResponse);
        Mockito.when(httpServerResponse.setStatusCode(Mockito.anyInt())).thenReturn(httpServerResponse);
        Assert.assertFalse(EnumValidator.validateHeader(routingContext, "ws-app", App.values(),
                App.class).isPresent());
    }

    @Test
    public final void shouldFailToValidateHeaderEnumOnInvalidHeader() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getHeader("ws-app")).thenReturn("WTF");
        Mockito.when(routingContext.response()).thenReturn(httpServerResponse);
        Mockito.when(httpServerResponse.setStatusCode(Mockito.anyInt())).thenReturn(httpServerResponse);
        Assert.assertFalse(EnumValidator.validateHeader(routingContext, "ws-app", App.values(),
                App.class).isPresent());
    }

    @Test
    public final void shouldValidatePathEnum() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getParam("app")).thenReturn("ACC");
        Assert.assertEquals(App.ACC, EnumValidator.validatePathParam(routingContext, "app", App.values(),
                App.class).get());
    }


    @Test
    public final void shouldFailToValidatePathEnumOnEmptyPath() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getParam("app")).thenReturn("");
        Mockito.when(routingContext.response()).thenReturn(httpServerResponse);
        Mockito.when(httpServerResponse.setStatusCode(Mockito.anyInt())).thenReturn(httpServerResponse);
        Assert.assertFalse(EnumValidator.validatePathParam(routingContext, "app", App.values(),
                App.class).isPresent());
    }

    @Test
    public final void shouldFailToValidatePathEnumOnInvalidPath() {
        Mockito.when(routingContext.request()).thenReturn(httpServerRequest);
        Mockito.when(httpServerRequest.getParam("app")).thenReturn("WTF");
        Mockito.when(routingContext.response()).thenReturn(httpServerResponse);
        Mockito.when(httpServerResponse.setStatusCode(Mockito.anyInt())).thenReturn(httpServerResponse);
        Assert.assertFalse(EnumValidator.validatePathParam(routingContext, "app", App.values(),
                App.class).isPresent());
    }
}
