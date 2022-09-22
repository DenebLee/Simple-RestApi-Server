package kr.nanoit.db.impl.todoservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.db.impl.TodoServicePostgreSqlQuerys;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.object.entity.TodoEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class TodoServicePostgreSQLImpl implements TodoService {
    private final PostgreSqlDbcp dbcp;

    public TodoServicePostgreSQLImpl(PostgreSqlDbcp dbcp) {
        this.dbcp = dbcp;
        createTable(TodoServicePostgreSqlQuerys.createTodoTable);
    }

    private void createTable(String query) {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // post 에 삭제된 시간을 삽입할 이유가 있는 지 생각중
    // String 으로 시간을 받는데 Date 로 받아도 되지않을까
    // SimpleDateFormat 을 String 으로 변환

    @Override
    public TodoEntity save(TodoEntity todoEntity) {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {

            Timestamp creatAt = Timestamp.valueOf(todoEntity.getCreatedAt());

            int affectRows = statement.executeUpdate(TodoServicePostgreSqlQuerys.insertTodo(creatAt, todoEntity.getContent()), statement.RETURN_GENERATED_KEYS);

            if (affectRows == 0) {
                return null;
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    todoEntity.setTodoId(resultSet.getLong(1));
                    return todoEntity;
                } else {
                    throw new CreateFailedException("not found result set");
                }
            }
        } catch (Exception e) {
            log.error("failed saved query", e);
        }
        return null;
    }

    @Override
    public TodoEntity findById(long todoId) {
        ResultSet resultSet = null;
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.selectTodo(todoId))) {
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                TodoEntity todoEntity = new TodoEntity();
                todoEntity.setTodoId(resultSet.getLong("id"));
                todoEntity.setCreatedAt(resultSet.getString("createAt"));
                todoEntity.setDeletedAt(resultSet.getString("deleteAt"));
                todoEntity.setContent(resultSet.getString("content"));

                return todoEntity;
            } else {
                throw new FindFailedException("not found result set");
            }
        } catch (Exception e) {
            log.error("failed found query", e);
        }
        return null;
    }

    @Override
    public boolean deleteById(long todoId) {
        try (Connection connection = dbcp.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.deleteTodo(todoId))) {
            int affectRow = preparedStatement.executeUpdate();
            if (affectRow == 0) {
                throw new DeleteException("There are no deleted rows");
            }
        } catch (Exception e) {
            log.error("failed delete query",e );
            return false;
        }
        return true;
    }

    @Override
    public TodoEntity update(TodoEntity todoEntity) {
        return null;
    }

    @Override
    public boolean containsById(long todoId) {
        return false;
    }
}
