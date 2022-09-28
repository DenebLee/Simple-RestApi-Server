package kr.nanoit.db.impl.todoservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.db.impl.TodoServicePostgreSqlQuerys;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.exception.UpdateException;
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


    @Override
    public TodoEntity save(TodoEntity todoEntity) throws SQLException {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {

            Timestamp creatAt = Timestamp.valueOf(todoEntity.getCreatedAt());

            int affectRows = statement.executeUpdate(TodoServicePostgreSqlQuerys.insertTodo(creatAt, todoEntity.getContent(), todoEntity.getWriter()), statement.RETURN_GENERATED_KEYS);

            if (affectRows == 0) {
                throw new SQLException("db error");
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        todoEntity.setTodoId(resultSet.getLong(1));
                        return todoEntity;
                    }
                } else {
                    throw new CreateFailedException("not found result set");
                }
            }
        } catch (SQLException e) {
            log.error("save query failed", e);
        }
        return null;
    }

    @Override
    public TodoEntity findById(long todoId) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.selectTodo(todoId))) {
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    TodoEntity todoEntity = new TodoEntity();
                    todoEntity.setTodoId(resultSet.getLong("id"));
                    todoEntity.setCreatedAt(resultSet.getString("createAt"));
                    todoEntity.setModifiedAt(resultSet.getString("modifiedAt"));
                    todoEntity.setContent(resultSet.getString("content"));
                    todoEntity.setWriter(resultSet.getString("writer"));
                    return todoEntity;
                }
            } else {
                throw new FindFailedException("not found result set");
            }
        } catch (SQLException e) {
            log.error("failed found query", e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
                throw new FindFailedException("resultSet.close failed");
            }
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
            log.error("failed delete query", e);
            return false;
        }
        return true;
    }

    @Override
    public TodoEntity update(TodoEntity todoEntity) {
        if (todoEntity != null) {
            try (Connection connection = dbcp.getConnection()) {
                Timestamp modifiedAt = Timestamp.valueOf(todoEntity.getModifiedAt());
                PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.updateTodo(todoEntity.getTodoId(), modifiedAt, todoEntity.getContent(), todoEntity.getWriter()));

                int affectRow = preparedStatement.executeUpdate();

                if (affectRow == 0) {
                    throw new UpdateException("no correction made");
                }
                todoEntity = findById(todoEntity.getTodoId());
                return todoEntity;
            } catch (Exception e) {
                log.error("failed updated query", e);
            }
        }
        return null;
    }

    @Override
    public boolean containsById(long todoId) {
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.containsById(todoId))) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    return resultSet.getInt("COUNT") == 1;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("failed updated query", e);
        }
        return false;
    }
}
