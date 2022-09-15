package kr.nanoit.db;

import kr.nanoit.object.config.DataBaseConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Testcontainers
class UserServicePostgreSQLImplTest {

    // STATIC 해당 클래스 테스트 내에서 컨테이너를 모두 공유
    // public 시 해당 클래스 테스트 각각 컨테이너를 따로 생성함
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.5-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void should_connect() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        String connurl = String.format("jdbc:postgresql://%s:%d/%s", postgreSQLContainer.getHost(), postgreSQLContainer.getFirstMappedPort(), "test");

        try (Connection connection = DriverManager.getConnection(connurl, postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())) {
            Statement stmt = connection.createStatement();
            // Select 1 AS PING
            // int ping = getInteger("PING)
            ResultSet rs = stmt.executeQuery("SELECT datname AS list FROM pg_database");
//            ResultSet rs = stmt.executeQuery("SELECT VERSION() AS version");

            while (rs.next()) {
                String version = rs.getString("list");
//                String version = rs.getString("version");
                System.out.println(version);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void should_user_service_connect() {
        UserService userService = UserService.createPostgreSQL(getDataBaseConfig());
    }

    private static DataBaseConfig getDataBaseConfig() {
        return new DataBaseConfig()
                .setIp(postgreSQLContainer.getHost())
                .setPort(postgreSQLContainer.getFirstMappedPort())
                .setDataBaseName(postgreSQLContainer.getDatabaseName())
                .setUsername(postgreSQLContainer.getUsername())
                .setPassword(postgreSQLContainer.getPassword());
    }
}