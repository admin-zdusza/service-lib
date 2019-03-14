package pl.zdusza.validators;

import java.nio.charset.Charset;

public class CharsetValidator {

    public final void validate() {
        if (!Charset.defaultCharset().equals(Charset.forName("UTF-8"))) {
            throw new RuntimeException("Invalid default charset:" + Charset.defaultCharset());
        }
        if (!System.getProperty("file.encoding").equals("UTF-8")) {
            throw new RuntimeException("Invalid file.encoding:" + System.getProperty("file.encoding"));
        }
    }
}
