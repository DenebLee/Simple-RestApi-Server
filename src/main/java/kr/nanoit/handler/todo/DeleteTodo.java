package kr.nanoit.handler.todo;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.object.dto.HttpResponseDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;
import static kr.nanoit.utils.GlobalVariable.HTTP_OK;


@Slf4j
public class DeleteTodo {
    private final TodoService todoService;

    public DeleteTodo(TodoService todoService) {this.todoService = todoService;}

    public void handle(HttpExchange exchange) {
        try{
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if(!queryStrings.containsKey("id")){
                badRequest(exchange,"not found: query.id");
            }

            if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) {
                badRequest(exchange, "invalid: query.id");
                return;
            }

            int todoId = Integer.parseInt(queryStrings.get("id").get(0));

            if(todoId <= 0){
                badRequest(exchange, "zero value: query.id");
                return;
            }

            if(!todoService.containsById(todoId)){
                badRequest(exchange, "not found: todo.id");
                return;
            }

            boolean isSuccess = todoService.deleteById(todoId);

            if(isSuccess){
                responseOk(exchange, Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_OK, null, "OK")).getBytes(StandardCharsets.UTF_8));
            }else{
                internalServerError(exchange, "delete fail: todo.id" + todoId);
            }
        } catch (Exception e) {
            log.error("delete handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
