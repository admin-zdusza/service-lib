package pl.zdusza;

import io.vertx.core.json.JsonObject;

import java.util.Optional;

import static java.lang.System.getenv;

public final class ConfigVariableResolver {

    private static final int USERNAME_INDEX = 3;
    private static final int PASSWORD_INDEX = 4;
    private static final int HOST_INDEX = 5;
    private static final int PORT_INDEX = 6;
    private static final int DATABASE_INDEX = 7;

    private ConfigVariableResolver() {
    }

    private static String requiredEnvVariable(final String key) {
        return Optional.ofNullable(getenv(key)).orElseGet(() -> {
            throw new IllegalArgumentException("Missing var : " + key);
        });
    }

    public static String requiredVariable(final JsonObject config, final String name) {
        return config.getString(name) != null
                && !config.getString(name).trim().isEmpty()
                ? config.getString(name) : requiredEnvVariable(name);
    }

    public static Integer requiredIntVariable(final JsonObject config, final String name) {
        return Integer.parseInt(requiredVariable(config, name));
    }

    public static Boolean requiredBoolVariable(final JsonObject config, final String name) {
        return Boolean.valueOf(requiredVariable(config, name));
    }

    public static JsonObject getVertxDatabaseConfig(final String process, final JsonObject config) {
        final String databaseUrl = requiredVariable(config, "DATABASE_URL");
        final String[] splittedArray = databaseUrl.split("[@/:]");
        return new JsonObject()
                .put("host", splittedArray[HOST_INDEX])
                .put("port", Integer.parseInt(splittedArray[PORT_INDEX]))
                .put("username", splittedArray[USERNAME_INDEX])
                .put("password", splittedArray[PASSWORD_INDEX])
                .put("database", splittedArray[DATABASE_INDEX])
                .put("sslMode", requiredVariable(config, "DATABASE_SSL_MODE"))
                .put("maxPoolSize", requiredIntVariable(config, process + "_DATABASE_MAX_POOL_SIZE"))
                .put("connectTimeout", requiredIntVariable(config, process + "_DATABASE_CONNECT_TIMEOUT"))
                .put("queryTimeout", requiredIntVariable(config, process + "_DATABASE_QUERY_TIMEOUT"));
    }

    public static JsonObject getFlywayDatabaseConfig(final String process, final JsonObject config) {
        JsonObject object = getVertxDatabaseConfig(process, config);
        return new JsonObject()
                .put("jdbcUrl", "jdbc:postgresql://" + object.getString("host") + ":"
                        + object.getInteger("port") + "/" + object.getString("database"))
                .put("username", object.getString("username"))
                .put("password", object.getString("password"));
    }
}
