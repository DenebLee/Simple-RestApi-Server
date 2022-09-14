package kr.nanoit.handler.todo;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.TodoService;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.utils.ExchangeRawPrinter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;

@Slf4j
public class GetTodo {

    private final TodoService todoService;
    public GetTodo(TodoService todoService) {
        this.todoService = todoService;
    }

    public void handle(HttpExchange exchange) {
        try {
            ExchangeRawPrinter.print(exchange);

            Map<String, List<String>> queryString = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryString.containsKey("id")) {
                badRequest(exchange, "null: query.id");
            }

            if (queryString.get("id").size() != 1) {
                badRequest(exchange, "invalid: query.id");
            }

            int todoUserId = Integer.parseInt(queryString.get("id").get(0));

            if (todoUserId <= 0) {
                badRequest(exchange, "zero value: query.id");
            }

            if (!todoService.containsById(todoUserId)) {
                badRequest(exchange, "not found: user.id");
            }

        } catch (Exception e) {
            log.error("Get handler error", e);
        }
    }
}
