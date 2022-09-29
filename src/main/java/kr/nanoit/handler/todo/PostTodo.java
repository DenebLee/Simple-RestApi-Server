package kr.nanoit.handler.todo;

import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.PostException;
import kr.nanoit.exception.HeaderBadRequestException;
import kr.nanoit.object.dto.TodoDto;
import kr.nanoit.object.entity.TodoEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

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

    // 무조건 여기서 결과값이 내려가야됨,
    // 무시되거나 리스폰스가 없거나 에러가 발생해서 상위 호출자에게 넘어가면 안됨
    public void handle(HttpExchange exchange) {
        try {
            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                throw new PostException("not found: Content-Type Header");
            }

            if (!exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                throw new PostException("accept Content-Type application/json");
            }

            String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody()));
            TodoDto todoDto = getRead(body);

            if (todoDto == null) {
                throw new PostException("Unacceptable value requested");
            }

            if (todoDto.getWriter() == null) {
                throw new PostException("not Found : Writer");
            }

            if (todoDto.getContent() == null) {
                throw new PostException("not Found : Content");
            }

            TodoEntity todoEntity = todoService.save(todoDto.toEntity());

            responseOk(exchange, Mapper.writePretty(todoEntity.toDto()).getBytes(StandardCharsets.UTF_8));

        } catch (PostException e) {
            badRequest(exchange, e.getReason());
        } catch (SQLException e) {
            if (e instanceof CreateFailedException) {
                badRequest(exchange, ((CreateFailedException) e).getReason());
            } else {
                badRequest(exchange, e.getMessage());
            }
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
