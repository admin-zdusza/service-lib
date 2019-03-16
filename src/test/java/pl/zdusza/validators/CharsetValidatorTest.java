package pl.zdusza.validators;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharsetValidatorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test()
    public final void testShouldCharsetBeValid() {
        new CharsetValidator().validate();
    }

    @Test()
    public final void testShouldCharsetBeInValid() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Invalid file.encoding: ISO");
        System.setProperty("file.encoding", "ISO");
        new CharsetValidator().validate();
    }

    @After
    public final void setEncoding() {
        System.setProperty("file.encoding", "UTF-8");
    }
}
