package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.object.dto.HttpResponseDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static kr.nanoit.utils.GlobalVariable.*;
import static kr.nanoit.handler.common.HandlerUtil.*;
import static kr.nanoit.handler.common.Validation.internalServerError;


@Slf4j
public final class DeleteUser {

    private final UserService userService;

    public DeleteUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryStrings.containsKey("id")) {
                notFound(exchange, "not found: query.id");
                return;
            }

            if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) {
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

            boolean isSuccess = userService.deleteById(userId);

            if (isSuccess) {
                responseOk(exchange, Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_OK, null, "OK")).getBytes(StandardCharsets.UTF_8));
            } else {
                internalServerError(exchange, "delete fail: user.id=" + userId);
            }
        } catch (Exception e) {
            log.error("delete handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
