package pl.zdusza.test;

import io.vertx.core.json.JsonObject;

public final class DBCommonConfig {

    private DBCommonConfig() {
    }

    public static JsonObject getDbCommonConf() {
        return getDbCommonConf("test", " test1234", "test", "test");
    }

    public static JsonObject getDbCommonConf(final String username, final String password, final String hostAndPort,
                                             final String databaseName) {
        return new JsonObject()
                .put("DATABASE_URL", "postgres://"
                        + username + ":"
                        + password + "@"
                        + hostAndPort + "/"
                        + databaseName)
                .put("DATABASE_SSL_MODE", "prefer");
    }
}
