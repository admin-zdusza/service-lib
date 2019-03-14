package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import org.flywaydb.core.Flyway;

import java.util.Optional;

import static pl.zdusza.ConfigVariableResolver.getFlywayDatabaseConfig;
import static pl.zdusza.ConfigVariableResolver.getVertxDatabaseConfig;

public final class DBClientCreator {

    private DBClientCreator() {
    }

    public static Future<SQLClient> createDBClient(final String process,
                                                   final Vertx vertx,
                                                   final Optional<Dao> dao,
                                                   final JsonObject config) {
        Future<SQLClient> future = Future.future();
        if (dao.isPresent()) {
            future.complete();
            return future;
        }
        SQLClient dbClient = PostgreSQLClient
                .createShared(vertx, getVertxDatabaseConfig(process, config));
        vertx.<SQLClient>executeBlocking(ftr -> {
            JsonObject flywayConfig = getFlywayDatabaseConfig(process, config);
            Flyway flyway = Flyway.configure()
                    .dataSource(flywayConfig.getString("jdbcUrl"),
                            flywayConfig.getString("username"),
                            flywayConfig.getString("password"))
                    .load();
            flyway.migrate();
            ftr.complete(dbClient);
        }, res -> {
            if (res.succeeded()) {
                future.complete(res.result());
            } else {
                future.fail(res.cause());
            }
        });
        return future;
    }
}
