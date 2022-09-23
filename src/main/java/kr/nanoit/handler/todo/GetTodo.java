package kr.nanoit.handler.todo;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.object.dto.TodoDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;

@Slf4j
public class GetTodo {

    private final TodoService todoService;
    public GetTodo(TodoService todoService) {
        this.todoService = todoService;
    }

    public void handle(HttpExchange exchange) {
        try {

            Map<String, List<String>> queryString = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryString.containsKey("id")) {
                badRequest(exchange, "null: query id");
                return;
            }

            if (queryString.get("id").size() != 1) {
                badRequest(exchange, "invalid: query.id");
                return;
            }

            int todoId = Integer.parseInt(queryString.get("id").get(0));

            if (todoId <= 0) {
                badRequest(exchange, "zero value: query.id");
                return;
            }

            if (!todoService.containsById(todoId)) {
                badRequest(exchange, "not found: todo.Id");
                return;
            }

            TodoDto todoDto = todoService.findById(todoId).toDto();

            if(todoDto == null){
                internalServerError(exchange, "get todo query failed : todoId =" + todoId);
                return;
            }

            responseOk(exchange, Mapper.writePretty(todoDto).getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
