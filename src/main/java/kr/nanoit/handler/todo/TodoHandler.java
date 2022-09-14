package kr.nanoit.handler.todo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kr.nanoit.db.TodoService;
import kr.nanoit.db.TodoServiceTestImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class TodoHandler implements HttpHandler {
    private final TodoService todoService;
    private final GetTodo getTodo;
    private final PostTodo postTodo;
    private final PatchTodo patchTodo;
    private final DeleteToto deleteToto;

    public TodoHandler() {
        todoService = new TodoServiceTestImpl();
        getTodo = new GetTodo(todoService);
        postTodo = new PostTodo(todoService);
        deleteToto = new DeleteToto(todoService);
        patchTodo = new PatchTodo(todoService);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case METHOD_GET:
                    getTodo.handle(exchange);
                case METHOD_POST:
                    postTodo.handle(exchange);
                case METHOD_DELETE:
                    deleteToto.handle(exchange);
                case METHOD_PATCH:
                    patchTodo.handle(exchange);
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