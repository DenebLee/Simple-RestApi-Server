package kr.nanoit.db.impl;

import kr.nanoit.object.config.DataBaseConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Testcontainers
@DisplayName("DBCP 를 만들고 DBCP 를 통해서 DATABASE 가 연결되는지 확인" +
        "VERSION 쿼리 확인" +
        "SELECT 1 등 핑 쿼리 확인" +
        "실제로 DB가 연결됐는지 확인")
class PostgreSqlDbcpTest {

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.5-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Test
    @DisplayName("DBCP CLASS 가 정상 생성 되는지 확인")
    void should_create() {
        assertThatCode(() -> {
            new PostgreSqlDbcp(getDataBaseConfig());
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("DBCP가 DataBase에 연결되는지 확인")
    void shoud_connect_db() throws Exception {
        PostgreSqlDbcp dbcp = new PostgreSqlDbcp(getDataBaseConfig());

        Statement stmt = null;
        ResultSet rset = null;

        try (Connection conn = dbcp.getConnection()) {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT 1");

            int numcols = rset.getMetaData().getColumnCount();
            while (rset.next()) {
                for (int i = 1; i <= numcols; i++) {
                    assertThat(numcols).isLessThanOrEqualTo(1);
                    assertThat(rset.getString(i)).isEqualTo("1");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (rset != null) rset.close();
            } catch (Exception e) {
                throw new Exception(e);
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
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