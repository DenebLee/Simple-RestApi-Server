package kr.nanoit.handler.todo;

import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.object.dto.TodoDto;
import kr.nanoit.object.entity.TodoEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;
import static kr.nanoit.utils.GlobalVariable.HEADER_CONTENT_TYPE;

@Slf4j
public class PostTodo {
    private final TodoService todoService;

    public PostTodo(TodoService todoService) {
        this.todoService = todoService;
    }

    public void handle(HttpExchange exchange) {
        try {
            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                badRequest(exchange, "not found: Content-Type Header");
                return;
            }

//            if (exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).isEmpty()) {
//                badRequest(exchange, "invalid: Content-Type Header");
//                return;
//            }

            if (!exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                badRequest(exchange, "accept Content-Type application/json");
                return;
            }

            String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody()));
            TodoDto todoDto = getRead(body);
            System.out.println(todoDto);

            if (todoDto == null) {
                badRequest(exchange, "parse failed");
                return;
            }

            if (todoDto.getCreatedAt() == null) {
                badRequest(exchange, "not Found : Created Time");
                return;
            }

            if (todoDto.getContent() == null) {
                badRequest(exchange, "not Found : Content");
                return;
            }

            TodoEntity todoEntity = todoService.save(todoDto.toEntity());

            responseOk(exchange, Mapper.writePretty(todoEntity.toDto()).getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            log.error("POST /todo handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }

    private TodoDto getRead(String body) {
        try {
            return Mapper.read(body, TodoDto.class);
        } catch (Exception e) {
            log.error("Json Read error", e);
            return null;
        }
    }
}
