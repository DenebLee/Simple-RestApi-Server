package kr.nanoit.db.impl.todoservice;

import kr.nanoit.object.entity.TodoEntity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TodoServiceTestImpl implements TodoService {
    private final AtomicLong todoId;
    private final ConcurrentHashMap<Long, TodoEntity> todoList;

    public TodoServiceTestImpl() {
        this.todoId = new AtomicLong(0);
        this.todoList = new ConcurrentHashMap<>();
    }

    @Override
    public TodoEntity save(TodoEntity todoEntity) {
        long key = todoId.incrementAndGet();
        todoEntity.setTodoId(key);
        todoList.put(key, todoEntity);
        return todoList.get(key);
    }

    @Override
    public TodoEntity findById(long todoId) {
        if (todoList.containsKey(todoId)) {
            return todoList.get(todoId);
        }
        return null;
    }

    @Override
    public boolean deleteById(long todoId) {
        return todoList.remove(todoId) != null;
    }

    @Override
    public TodoEntity update(TodoEntity todoEntity) {
        if (todoList.containsKey(todoEntity.getTodoId())) {
            todoList.put(todoEntity.getTodoId(), todoEntity);
            return todoList.get(todoEntity.getTodoId());
        }
        return null;
    }

    @Override
    public boolean containsById(long todoId) {
        return todoList.containsKey(todoId);
    }
}
