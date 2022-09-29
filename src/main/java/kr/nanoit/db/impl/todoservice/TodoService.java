package kr.nanoit.db.impl.todoservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.exception.UpdateException;
import kr.nanoit.object.entity.TodoEntity;

import java.sql.SQLException;

public interface TodoService {

    static TodoService createTest() {
        return new TodoServiceTestImpl();
    }
    static TodoService createPostgreSQL(PostgreSqlDbcp dbcp) { return new TodoServicePostgreSQLImpl(dbcp);}

    TodoEntity save(TodoEntity todoEntity) throws SQLException;

    TodoEntity findById(long todoId) throws FindFailedException;


    boolean deleteById(long todoId) throws DeleteException;

    TodoEntity update(TodoEntity todoEntity) throws UpdateException;
    boolean containsById(long todoId);
}
