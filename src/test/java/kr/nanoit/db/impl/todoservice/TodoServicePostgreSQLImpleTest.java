package kr.nanoit.db.impl.todoservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.object.config.DataBaseConfig;
import kr.nanoit.object.entity.TodoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class TodoServicePostgreSQLImpleTest {
    private PostgreSqlDbcp dbcp;
    private TodoService todoService;

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.5-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        dbcp = new PostgreSqlDbcp(getDataBaseConfig());
        todoService = TodoService.createPostgreSQL(dbcp);
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
    }

    @Test
    @DisplayName("TodoService 테이블 생성 되어야 함")
    void should_create_todoService_table() {
        // given : BeforeEach
        // when : BeforeEach
        // then
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT table_name from information_schema.tables")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            assertThat(resultSet.getMetaData().getColumnCount()).isGreaterThanOrEqualTo(1);

            boolean isCreated = false;
            while (resultSet.next()) {
                if (resultSet.getString(1).equals("todo")) {
                    isCreated = true;
                }
            }
            assertThat(isCreated).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("TodoServicePostgreSQLImpleTest TodoEntity 정상적으로 저장되는지 확인")
    void should_save() {
        // given
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
        TodoEntity expected = new TodoEntity(0, createAt, null, "세이브 되는지 체크하고 있어요", true);

        // when
        TodoEntity actual = todoService.save(expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getTodoId()).isEqualTo(expected.getTodoId());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
        assertThat(actual).isNotNull();

    }

    @Test
    @DisplayName("TodoServicePostgreSQLImpleTest TodoEntity 정상적으로 조회 되는지")
    void should_findById() {
        // given
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
        TodoEntity expected = todoService.save(new TodoEntity(0, createAt, null, "안녕하세요 오늘은 시간 변환 때문에 힘들었어요", true));

        // when
        TodoEntity actual = todoService.findById(expected.getTodoId());

        // then
        assertThat(actual.getTodoId()).isEqualTo(expected.getTodoId());
        assertThat(actual.getCreatedAt()).isEqualTo(expected.getCreatedAt());
        assertThat(actual.getContent()).isEqualTo(expected.getContent());
    }

    @Test
    @DisplayName("TodoServicePostgreImpleTest TodoEntity 정상적으로 삭제 되는지")
    void should_delete() {
        // given
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
        TodoEntity expected = todoService.save(new TodoEntity(0, createAt, null, "삭제 테스트 중입니다. 삭제 되었으면 좋겠네요", false));

        // when
        boolean actual = todoService.deleteById(expected.getTodoId());

        // then
        assertThat(actual).isTrue();
        assertThat(todoService.findById(expected.getTodoId())).isNull();
    }

    @Test
    @DisplayName("TodoServicePostgreimpleTest TodoEntity 정상적으로 수정 되는지")
    void should_update() {
        // given
        String modifiedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());

        TodoEntity originalTodoDataExpected = todoService.save(new TodoEntity(0, createAt , modifiedAt, "수정전 투두리스트입니다", false));
        TodoEntity updateExpected = new TodoEntity(originalTodoDataExpected.getTodoId(), createAt, modifiedAt, "수정된 내용입니다 하하하하", true);

        // when
        TodoEntity actual = todoService.update(updateExpected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getTodoId()).isEqualTo(updateExpected.getTodoId());
        assertThat(actual.getCreatedAt()).isEqualTo(updateExpected.getCreatedAt());
        assertThat(actual.getModifiedAt()).isEqualTo(updateExpected.getModifiedAt());
        assertThat(actual.getContent()).isEqualTo(updateExpected.getContent());
        assertThat(actual.isCompleted()).isEqualTo(updateExpected.isCompleted());
        assertThat(actual).isNotEqualTo(originalTodoDataExpected);
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
