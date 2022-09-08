package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.extension.Variable.*;
import static kr.nanoit.utils.HandlerUtil.*;
import static kr.nanoit.utils.Validation.*;

@Slf4j
public class GetUser {

    private final UserService userService;

    public GetUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryStrings.containsKey("id")) { // ID 가 없을때
                notFound(exchange, "not found: query.id");
                return;
            }

            if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) { // ID 가 있고 id size가 1이 아닐때
                badRequest(exchange, "invalid: query.id");
                return;
            }


            int userId = Integer.parseInt((queryStrings.get("id").get(0)));

            if (userId == 0) {
                badRequest(exchange, "zero value: query.id");
                return;
            }

            if (!userService.containsById(userId)) {
                notFound(exchange, "not found: user.id");
                return;
            }

            UserDto userDto = userService.findById(userId).toDto();

            if (userDto == null) {
                internalServerError(exchange, "get user query failed: user.id=" + userId);
                return;
            }

            responseOk(exchange, Mapper.writePretty(userDto).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }


}
