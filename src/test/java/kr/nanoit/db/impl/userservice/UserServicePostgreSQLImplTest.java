package kr.nanoit.db.impl.userservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.exception.UpdateException;
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
    @DisplayName("UserService 테이블이 생성 되어야함 ")
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
    void should_save() throws CreateFailedException, FindFailedException {
        // given
        UserEntity expected = createTestUserEntity();

        // when
        UserEntity actual = userService.save(expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
        assertThat(userService.findById(actual.getId())).isNotNull();
    }

    @Test
    @DisplayName("UserServicePostgreSQLImpleTest UserEntity 정상적으로 조회 되는지")
    void should_findById() throws CreateFailedException, FindFailedException {
        // given
        UserEntity expected = userService.save(new UserEntity(0,"test", "123123", "ee@ee.com"));

        // when
        UserEntity actual = userService.findById(expected.getId());

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    @DisplayName("UserServicePostgreSQLImpleTest userEntity 정상적으로 삭제 되는지")
    void should_delete() throws CreateFailedException, DeleteException, FindFailedException {
        // given
        UserEntity expected = userService.save(new UserEntity(0,"elel", "4444", "fdf@fdf.com"));

        // when
        boolean actual = userService.deleteById(expected.getId());

        // then
        assertThat(actual).isTrue();
        assertThat(userService.findById(expected.getId())).isNull();
    }

    @Test
    @DisplayName("UserServicePostgreSQLImpleTest userEntity 정상적으로 수정 되는지")
    void should_update() throws UpdateException, CreateFailedException {
        // given
        UserEntity originalUserDataExpected = userService.save(new UserEntity(0,"tete","123123", "t3t@aaa.com"));
        UserEntity updateExpected = new UserEntity(originalUserDataExpected.getId(), "LeeJeongSeob", "444", "post@naver.com");

        // when
        UserEntity actual = userService.update(updateExpected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(updateExpected.getId());
        assertThat(actual.getUsername()).isEqualTo(updateExpected.getUsername());
        assertThat(actual.getPassword()).isEqualTo(updateExpected.getPassword());
        assertThat(actual.getEmail()).isEqualTo(updateExpected.getEmail());
        assertThat(actual).isNotEqualTo(originalUserDataExpected);
    }

    private static UserEntity createTestUserEntity() {
        return new UserEntity(0, "lee", "123123", "test@test.com");
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