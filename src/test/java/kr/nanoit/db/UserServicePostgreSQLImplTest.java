package kr.nanoit.db;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.object.config.DataBaseConfig;
import kr.nanoit.object.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
class UserServicePostgreSQLImplTest {
    private PostgreSqlDbcp dbcp;

    private UserService userService;


    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.5-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        dbcp = new PostgreSqlDbcp(getDataBaseConfig());
        userService = UserService.createPostgreSQL(dbcp);
    }

    @Test
    @DisplayName("UserService 테이블이 생성 되야함 ")
    void should_create_table() throws Exception {
        // given: BeforeEach
        // when: BeforeEach
        // then
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select table_name from information_schema.tables");

            assertThat(resultSet.getMetaData().getColumnCount()).isGreaterThanOrEqualTo(1);

            boolean isCreated = false;
            while (resultSet.next()) {
                if (resultSet.getString(1).equals("users")) {
                    isCreated = true;
                }
            }
            assertThat(isCreated).isTrue();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    @DisplayName("UserServicePostgreSQLImpleTest UserEntity 정상적으로 저장되는지 확인")
    void should_save() throws ClassNotFoundException {
        // given
        UserEntity expected = new UserEntity(0, "lee", "123123" ,"test@test.com");

        // when
        UserEntity actual = userService.save(expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("UserServicePostgreSQLImpleTest UserEntity 정상적으로 조회 되는지")
    void should_findId()  {
        // given
        UserEntity testData = new UserEntity(0,"lee", "123123", "test@test.com");
        UserEntity expected =  userService.save(testData);

        // when
        UserEntity actual = userService.findById(expected.getId());

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
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