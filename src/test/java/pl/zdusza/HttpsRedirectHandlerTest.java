package pl.zdusza;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.junit.Test;
import org.mockito.Mockito;

public class HttpsRedirectHandlerTest {

    @Test
    public final void testShouldRedirect() {
        final RoutingContext context = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        Mockito.when(context.request()).thenReturn(request);
        Mockito.when(request.getHeader("X-Forwarded-Proto")).thenReturn("http");
        Mockito.when(request.absoluteURI()).thenReturn("http://absolute.com");
        final HttpServerResponse response = Mockito.mock(HttpServerResponse.class);
        Mockito.when(context.response()).thenReturn(response);
        Mockito.when(response.setStatusCode(Statuses.MOVED_PERMANENTLY)).thenReturn(response);
        Mockito.when(response.putHeader("Location", "https://absolute.com")).thenReturn(response);
        new HttpsRedirectHandler(false).handle(context);
        Mockito.verify(context.response()).setStatusCode(Statuses.MOVED_PERMANENTLY);
        Mockito.verify(context.response()).putHeader("Location", "https://absolute.com");
    }

    @Test
    public final void testShouldDisable() {
        final RoutingContext context = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        Mockito.when(context.request()).thenReturn(request);
        Mockito.when(request.getHeader("X-Forwarded-Proto")).thenReturn("http");
        new HttpsRedirectHandler(true).handle(context);
        Mockito.verify(context).next();
    }
}
