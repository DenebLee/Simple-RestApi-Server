package kr.nanoit.handler.todo;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;

import java.io.IOException;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.utils.GlobalVariable.HEADER_CONTENT_TYPE;

public class PostTodo {
    private final TodoService todoService;

    public PostTodo(TodoService todoService) {
        this.todoService = todoService;
    }

    public void handle(HttpExchange exchange) {
        try{
            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                badRequest(exchange, "not found: Content-Type Header");
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
