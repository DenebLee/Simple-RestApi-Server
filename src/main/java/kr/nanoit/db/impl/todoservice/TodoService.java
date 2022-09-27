package kr.nanoit.db.impl.todoservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.object.entity.TodoEntity;

import java.sql.SQLException;

public interface TodoService {

    static TodoService createTest() {
        return new TodoServiceTestImpl();
    }
    static TodoService createPostgreSQL(PostgreSqlDbcp dbcp) { return new TodoServicePostgreSQLImpl(dbcp);}

    TodoEntity save(TodoEntity todoEntity) throws SQLException;

    TodoEntity findById(long todoId) throws SQLException;


    boolean deleteById(long todoId);

    TodoEntity update(TodoEntity todoEntity);
    boolean containsById(long todoId);
}
