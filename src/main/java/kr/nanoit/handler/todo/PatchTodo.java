package kr.nanoit.handler.todo;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.exception.DtoReadException;
import kr.nanoit.exception.HeaderBadRequestException;
import kr.nanoit.exception.UpdateException;
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
public class PatchTodo {
    private final TodoService todoService;

    public PatchTodo(TodoService todoService) {
        this.todoService = todoService;
    }

    public void handle(HttpExchange exchange) {
        try {
            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                throw new UpdateException("not found: Content-Type Header");
            }

            if (!exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                throw new UpdateException("accept Content-Type: application/json");
            }

            String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody(), Charsets.UTF_8));
            TodoDto todoDto = getRead(body);

            if (todoDto == null) {
                throw new DtoReadException("parse failed");
            }
            if (todoDto.getTodoId() == 0) {
                throw new DtoReadException("TodoID is missing");
            }

            TodoEntity todoEntity = todoService.update(todoDto.toEntity());

            if (todoEntity == null) {
                throw new Exception("Unknown Error");
            }

            responseOk(exchange, Mapper.writePretty(todoEntity).getBytes(StandardCharsets.UTF_8));


        } catch (DtoReadException e) {
            badRequest(exchange, e.getReason());
        } catch (UpdateException e) {
            badRequest(exchange, e.getReason());
        } catch (Exception e) {
            log.error("patch handler error occurred", e);
            internalServerError(exchange, e.getMessage());
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
