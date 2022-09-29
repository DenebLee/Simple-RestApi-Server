package kr.nanoit.handler.todo;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.exception.DtoReadException;
import kr.nanoit.exception.GetException;
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
                throw new GetException("null: query id");
            }

            if (queryString.get("id").size() != 1) {
                throw new GetException("invalid: query.id");
            }

            int todoId = Integer.parseInt(queryString.get("id").get(0));

            if (todoId <= 0) {
                throw new GetException("zero value: query.id");
            }

            if (!todoService.containsById(todoId)) {
                throw new GetException("not found: todo.Id");
            }

            TodoDto todoDto = todoService.findById(todoId).toDto();

            if (todoDto == null) {
                throw new DtoReadException("get user query failed: user.id=" + todoId);
            }

            responseOk(exchange, Mapper.writePretty(todoDto).getBytes(StandardCharsets.UTF_8));

        } catch (GetException e) {
            badRequest(exchange, e.getReason());
        } catch (DtoReadException e) {
            badRequest(exchange, e.getReason());
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
