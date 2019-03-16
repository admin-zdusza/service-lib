package pl.zdusza;

import io.vertx.core.json.JsonObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static pl.zdusza.ConfigVariableResolver.getFlywayDatabaseConfig;
import static pl.zdusza.ConfigVariableResolver.getVertxDatabaseConfig;
import static pl.zdusza.ConfigVariableResolver.requiredIntVariable;
import static pl.zdusza.ConfigVariableResolver.requiredVariable;

public class ConfigVariableResolverTest {

    private static final int PORT = 5432;
    private static final int TIMEOUT = 100;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void testShouldGetVariableFromConfig() {
        final String key = "TEST_KEY";
        final String val = "TEST_VAL";
        assertEquals(val, requiredVariable(new JsonObject().put(key, val), key));
    }

    @Test
    public final void testShouldGetIntVariableFromConfig() {
        final String key = "TEST_KEY";
        final String val = "1";
        assertEquals(valueOf(parseInt(val)), requiredIntVariable(new JsonObject().put(key, val), key));
    }

    @Test
    public final void testShouldFailToGetVariableFromConfigAndEnv() {
        thrown.expectMessage("Missing var : TEST_KEY");
        requiredVariable(new JsonObject(), "TEST_KEY");
    }

    @Test
    public final void testShouldFailToGetIntVariableFromConfigAndEnv() {
        thrown.expectMessage("Missing var : TEST_KEY");
        requiredIntVariable(new JsonObject(), "TEST_KEY");
    }

    @Test
    public final void testShouldGetVertxDatabaseConfig() {
        assertEquals(new JsonObject()
                        .put("host", "dummy")
                        .put("port", PORT)
                        .put("username", "dummy")
                        .put("password", "dummy")
                        .put("database", "dummy")
                        .put("sslMode", "true")
                        .put("maxPoolSize", 1)
                        .put("connectTimeout", TIMEOUT)
                        .put("queryTimeout", TIMEOUT),
                getVertxDatabaseConfig("TEST", new JsonObject()
                        .put("DATABASE_URL", "postgres://dummy:dummy@dummy:5432/dummy")
                        .put("DATABASE_SSL_MODE", "true")
                        .put("TEST_DATABASE_MAX_POOL_SIZE", "1")
                        .put("TEST_DATABASE_CONNECT_TIMEOUT", "100")
                        .put("TEST_DATABASE_QUERY_TIMEOUT", "100")));
    }

    @Test
    public final void testShouldGetFlywayDatabaseConfig() {
        assertEquals(new JsonObject()
                        .put("jdbcUrl", "jdbc:postgresql://dummy:5432/dummy")
                        .put("username", "dummy")
                        .put("password", "dummy"),
                getFlywayDatabaseConfig("TEST", new JsonObject()
                        .put("DATABASE_URL", "postgres://dummy:dummy@dummy:5432/dummy")
                        .put("DATABASE_SSL_MODE", "true")
                        .put("TEST_DATABASE_MAX_POOL_SIZE", "1")
                        .put("TEST_DATABASE_CONNECT_TIMEOUT", "100")
                        .put("TEST_DATABASE_QUERY_TIMEOUT", "100")));
    }
}
