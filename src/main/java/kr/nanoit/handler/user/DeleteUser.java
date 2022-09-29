package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.exception.DeleteException;
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
public final class DeleteUser {

    private final UserService userService;

    public DeleteUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            if (!queryStrings.containsKey("id")) {
                throw new DeleteException("not found: query.id");
            }

            if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) {
                throw new DeleteException("invalid: query.id");
            }

            int userId = Integer.parseInt(queryStrings.get("id").get(0));

            if (userId <= 0) {
                throw new DeleteException("zero value: query.id");
            }

            if (!userService.containsById(userId)) {
                throw new DeleteException("not found: user.id");
            }

            boolean isSuccess = userService.deleteById(userId);

            if (isSuccess) {
                responseOk(exchange, Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_OK, null, "OK")).getBytes(StandardCharsets.UTF_8));
            } else {
                internalServerError(exchange, "delete fail: user.id=" + userId);
            }

        } catch (DeleteException e) {
            log.error("delete handler error occurred", e);
            badRequest(exchange, e.getReason());
        } catch (Exception e) {
            log.error("delete handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
