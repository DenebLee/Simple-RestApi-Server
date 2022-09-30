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
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    public TodoEntity save(TodoEntity todoEntity) throws CreateFailedException {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {
            // current time set
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
            Timestamp createdAt = Timestamp.valueOf(currentTime);

            int affectRows = statement.executeUpdate(TodoServicePostgreSqlQuerys.insertTodo(createdAt, todoEntity.getContent(), todoEntity.getWriter()), statement.RETURN_GENERATED_KEYS);

            if (affectRows == 0) {
                throw new SQLException("Failed to save to database");
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    todoEntity.setTodoId(resultSet.getLong(1));
                    todoEntity.setCreatedAt(String.valueOf(createdAt));
                    return todoEntity;
                } else {
                    throw new CreateFailedException("not found result set");
                }
            }
        } catch (CreateFailedException e) {
            throw new CreateFailedException(e.getReason());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Post server error");
        }
    }

    @Override
    public TodoEntity findById(long todoId) throws FindFailedException {
        ResultSet resultSet;
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.selectTodo(todoId))) {
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                TodoEntity todoEntity = new TodoEntity();
                todoEntity.setTodoId(resultSet.getLong("id"));
                todoEntity.setCreatedAt(resultSet.getString("createAt"));
                todoEntity.setModifiedAt(resultSet.getString("modifiedAt"));
                todoEntity.setContent(resultSet.getString("content"));
                todoEntity.setWriter(resultSet.getString("writer"));
                return todoEntity;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("failed found query", e);
            throw new FindFailedException("failed found query");
        }
    }

    @Override
    public boolean deleteById(long todoId) throws DeleteException {
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TodoServicePostgreSqlQuerys.deleteTodo(todoId))) {

            int affectRow = preparedStatement.executeUpdate();
            if (affectRow == 0) {
                throw new DeleteException("There are no deleted rows");
            }
        } catch (DeleteException e) {
            log.error("failed delete query", e);
            throw new DeleteException(e.getReason());
        } catch (Exception e) {
            log.error("failed delete query", e);
            return false;
        }
        return true;
    }

    @Override
    public TodoEntity update(TodoEntity todoEntity) throws UpdateException {
        if (todoEntity != null) {
            try (Connection connection = dbcp.getConnection();
                 Statement statement = connection.createStatement();) {
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(System.currentTimeMillis());
                Timestamp updateTime = Timestamp.valueOf(currentTime);
                int affectRow = 0;

                if (todoEntity.getWriter() != null) {
                    affectRow = statement.executeUpdate(TodoServicePostgreSqlQuerys.updateTodo(updateTime, "writer", todoEntity.getWriter(), todoEntity.getTodoId()));
                }
                if (todoEntity.getContent() != null) {
                    affectRow = statement.executeUpdate(TodoServicePostgreSqlQuerys.updateTodo(updateTime, "content", todoEntity.getContent(), todoEntity.getTodoId()));
                }
                if (affectRow == 0) {
                    throw new UpdateException("no correction made");
                }
                todoEntity = findById(todoEntity.getTodoId());
                statement.close();
                return todoEntity;

            } catch (UpdateException e) {
                log.error("failed updated query", e);
                throw new UpdateException(e.getReason());
            } catch (Exception e) {
                log.error("failed updated query", e);
                throw new UpdateException(e.getMessage());
            }
        }
        throw new UpdateException("Update server error");
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
