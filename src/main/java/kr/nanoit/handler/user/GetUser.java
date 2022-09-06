package kr.nanoit.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.domain.UserDto;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.extension.RelatedBody;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.nanoit.extension.Variable.*;

/**
 * 클래스 한개는 하나의 일만 한다.
 * <p>
 * 1. 정의
 * <p>
 * 성공 EXAMPLE
 * GET /user?id=1
 * <p>
 * 실패 EXAMPLE
 * query id가 없는 경우 모두 실패
 */

@Slf4j
public class GetUser {

    private final UserService userService;

    public GetUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            print(exchange);

            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());
            if (isInvalid(exchange, queryStrings)) return;

            UserDto userDto = userService.getUser(Integer.parseInt(queryStrings.get("id").get(0)));
            if (userDto == null) {
                badRequest(exchange);
                return;
            }

            String raw = userDto.toString();
            log.info("{}", raw);
            responseOk(exchange, raw.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("get handler error occurred", e);
        } finally {
            exchange.close();
        }
    }

    private void print(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        System.out.println(RelatedBody.parseBody(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))));
    }

    private void responseOk(HttpExchange exchange, byte[] rawBytes) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        exchange.sendResponseHeaders(STATUS_OK, rawBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(rawBytes);
        outputStream.flush();
    }

    private boolean isInvalid(HttpExchange exchange, Map<String, List<String>> queryStrings) throws IOException, JSONException {
        for (Map.Entry<String, List<String>> stringListEntry : queryStrings.entrySet()) {
            log.info("KEY={} VALUES={}", stringListEntry.getKey(), stringListEntry.getValue().stream().map(value -> "[" + value + "]").collect(Collectors.joining(",")));
        }
        if (!queryStrings.containsKey("id")) { // ID 가 없을때
            badRequest(exchange);
            return true;
        }

        if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) { // ID 가 있고 id size가 1이 아닐때
            badRequest(exchange);
            return true;
        }

        return false;
    }

    public void badRequest(HttpExchange exchange) throws IOException, JSONException {
        exchange.sendResponseHeaders(STATUS_FAILD, -1);
        byte[] bytesOfBody = BadRequestWord.getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(bytesOfBody);
        outputStream.flush();
    }
}
