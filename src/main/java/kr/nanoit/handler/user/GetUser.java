package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.exception.DtoReadException;
import kr.nanoit.exception.GetException;
import kr.nanoit.handler.common.QueryParsing;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.ExchangeRawPrinter;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;

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
                throw new GetException("null: query.id");
            }

            if (queryStrings.get("id").size() != 1) {
                throw new GetException("invalid: query.id");
            }

            int userId = Integer.parseInt(queryStrings.get("id").get(0));

            if (userId <= 0) {
                throw new GetException("zero value: query.id");
            }

            if (!userService.containsById(userId)) {
                throw new GetException("not found: user.id");
            }

            UserDto userDto = userService.findById(userId).toDto();

            if (userDto == null) {
                throw new DtoReadException("get user query failed: user.id=" + userId);
            }
            responseOk(exchange, Mapper.writePretty(userDto).getBytes(StandardCharsets.UTF_8));
        } catch (GetException | DtoReadException e) {
            badRequest(exchange, e.getMessage());
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }


}
