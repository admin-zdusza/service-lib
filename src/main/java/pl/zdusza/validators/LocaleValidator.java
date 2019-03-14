package pl.zdusza.validators;

import java.util.Locale;

public class LocaleValidator {

    public final void validate() {
        if (!Locale.getDefault().equals(new Locale("pl", "PL"))) {
            throw new RuntimeException("Invalid locale:" + Locale.getDefault());
        }
    }
}
