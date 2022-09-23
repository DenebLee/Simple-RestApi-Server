package kr.nanoit.handler.todo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kr.nanoit.db.impl.todoservice.TodoService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class TodoHandler implements HttpHandler {
    private final TodoService todoService;
    private final GetTodo getTodo;
    private final PostTodo postTodo;
    private final PatchTodo patchTodo;
    private final DeleteTodo deleteTodo;

    public TodoHandler(TodoService todoService) {
        this.todoService = todoService;
        this.getTodo = new GetTodo(todoService);
        this.postTodo = new PostTodo(todoService);
        this.deleteTodo = new DeleteTodo(todoService);
        this.patchTodo = new PatchTodo(todoService);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case METHOD_GET:
                    getTodo.handle(exchange);
                    break;
                case METHOD_POST:
                    postTodo.handle(exchange);
                    break;
                case METHOD_DELETE:
                    deleteTodo.handle(exchange);
                    break;
                case METHOD_PATCH:
                    patchTodo.handle(exchange);
                    break;
                default:
                    badRequest(exchange);
                    break;
            }
        } catch (Exception e) {
            log.error("handler error", e);
        }
    }
    private static void badRequest(HttpExchange exchange){
        try {
            Headers headers = exchange.getResponseHeaders();
            headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
            exchange.sendResponseHeaders(405, -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}