package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.ExchangeRawPrinter;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.*;
import static kr.nanoit.handler.common.Validation.*;

@Slf4j
public class GetUser {

    private final UserService userService;

    public GetUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            ExchangeRawPrinter.print(exchange);

            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryStrings.containsKey("id")) {
                badRequest(exchange, "null: query.id");
                return;
            }

            if (queryStrings.get("id").size() != 1) {
                badRequest(exchange, "invalid: query.id");
                return;
            }

            int userId = Integer.parseInt(queryStrings.get("id").get(0));

            if (userId <= 0) {
                badRequest(exchange, "zero value: query.id");
                return;
            }

            if (!userService.containsById(userId)) {
                badRequest(exchange, "not found: user.id");
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
