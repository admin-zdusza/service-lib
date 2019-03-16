package pl.zdusza.validators;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Locale;

public class LocaleValidatorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test()
    public final void testShouldLocaleBeValid() {
        new LocaleValidator().validate();
    }

    @Test()
    public final void testShouldLocaleBeInValid() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Invalid locale: en_CA");
        Locale.setDefault(Locale.CANADA);
        new LocaleValidator().validate();
    }

    @After
    public final void setLocale() {
        Locale.setDefault(new Locale("pl", "PL"));
    }
}
