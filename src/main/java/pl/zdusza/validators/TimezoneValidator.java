package pl.zdusza.validators;

import java.time.ZoneId;
import java.util.TimeZone;

public class TimezoneValidator {

    public final void validate() {
        if (!TimeZone.getDefault().equals(TimeZone.getTimeZone(ZoneId.of("UTC")))) {
            throw new RuntimeException("Invalid timezone: " + TimeZone.getDefault());
        }
    }
}
