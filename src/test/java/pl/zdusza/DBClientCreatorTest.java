package pl.zdusza;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import pl.zdusza.test.DBCommonConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

@RunWith(VertxUnitRunner.class)
public class DBClientCreatorTest {

    @Rule
    public RunTestOnContext vertxContext = new RunTestOnContext();

    @Rule
    public PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:10.5");

    @Test
    public final void testShouldCreateClient(final TestContext tc) {
        Async async = tc.async();
        JsonObject commonConf = DBCommonConfig.getDbCommonConf(postgres.getUsername(),
                postgres.getPassword(),
                postgres.getJdbcUrl().split("/")[2],
                postgres.getDatabaseName());
        Future<SQLClient> f = DBClientCreator.createDBClient("TEST", vertxContext.vertx(), Optional.empty(),
                new JsonObject()
                        .mergeIn(commonConf)
                        .put("TEST_DATABASE_MAX_POOL_SIZE", "1")
                        .put("TEST_DATABASE_CONNECT_TIMEOUT", "100")
                        .put("TEST_DATABASE_QUERY_TIMEOUT", "100")
        );
        f.setHandler(v -> {
            tc.assertTrue(v.succeeded());
            async.complete();
        });
    }

    @Test
    public final void testShouldSkipCreateClientWhenDaoProvided(final TestContext tc) {
        Async async = tc.async();
        JsonObject commonConf = DBCommonConfig.getDbCommonConf(postgres.getUsername(),
                postgres.getPassword(),
                postgres.getJdbcUrl().split("/")[2],
                postgres.getDatabaseName());
        Future<SQLClient> f = DBClientCreator.createDBClient("TEST", vertxContext.vertx(),
                Optional.of(Mockito.mock(Dao.class)),
                new JsonObject()
                        .mergeIn(commonConf)
                        .put("TEST_DATABASE_MAX_POOL_SIZE", "1")
                        .put("TEST_DATABASE_CONNECT_TIMEOUT", "100")
                        .put("TEST_DATABASE_QUERY_TIMEOUT", "100")
        );
        f.setHandler(v -> {
            tc.assertTrue(v.succeeded());
            async.complete();
        });
    }
}
