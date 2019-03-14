package pl.zdusza;

import io.vertx.core.json.JsonObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static pl.zdusza.ConfigVariableResolver.requiredIntVariable;
import static pl.zdusza.ConfigVariableResolver.requiredVariable;

public class ConfigVariableResolverTest {

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
}
