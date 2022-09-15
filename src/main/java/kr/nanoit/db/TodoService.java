package kr.nanoit.db;

import kr.nanoit.db.impl.TodoServiceTestImpl;
import kr.nanoit.object.entity.TodoEntity;

public interface TodoService {

    static TodoService createTest() {
        return new TodoServiceTestImpl();
    }

    TodoEntity save(TodoEntity todoEntity);

    TodoEntity findById(long todoId);

    boolean deleteById(long todoId);

    TodoEntity update(TodoEntity todoEntity);

    boolean containsById(long todoId);
}
