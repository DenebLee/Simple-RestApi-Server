package kr.nanoit.handler.user;


import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.utils.HandlerUtil.outPutStream;
import static kr.nanoit.utils.Validation.*;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.object.dto.HttpResponse;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import com.sun.net.httpserver.Headers;

import static kr.nanoit.extension.Variable.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeleteUser {

    private UserService userService;

    DeleteUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());
            if (isInvalid(exchange, queryStrings)) return;
            int userId = Integer.parseInt(queryStrings.get("id").get(0));
            if (userService.isDuplication(exchange, userId)) return;
            userService.deleteById(userId);
            deleteResponseOk(exchange);
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }

    private void deleteResponseOk(HttpExchange exchange) throws IOException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponse(OffsetDateTime.now().toString(), HTTP_OK, "Delete Success", "OK")).getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        exchange.sendResponseHeaders(HTTP_OK, bytesOfBody.length);
        outPutStream(exchange, bytesOfBody);
    }
}
