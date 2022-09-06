package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.extension.Variable.*;
import static kr.nanoit.utils.HandlerUtil.print;
import static kr.nanoit.utils.HandlerUtil.responseOk;
import static kr.nanoit.utils.Validation.*;

@Slf4j
public class GetUser {

    private  UserService userService;

    public GetUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            print(exchange);

            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());
            if (isInvalid(exchange, queryStrings)) return;
            UserEntity userEntity = userService.findById(Integer.parseInt(queryStrings.get("id").get(0)));
            UserDto userDto = userEntity.toDto();
            if (isNull(exchange, userDto)) return;

            responseOk(exchange, Mapper.writePretty(userDto).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }

    private boolean IsNotContainsJson(HttpExchange exchange) throws IOException {
        if (exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE) && exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).size() > 0) {
            if (exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                return false;
            }
        }
        badRequest(exchange, "not found: Content-Type Header");
        return true;
    }

    private boolean isNull(HttpExchange exchange, UserDto userDto) throws IOException {
        if (userDto == null) {
            badRequest(exchange, "parse failed");
            return true;
        }
        return false;
    }






}
