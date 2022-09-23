package kr.nanoit.handler.common;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.dto.HttpResponseDto;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class Validation {

    public Validation() {
    }

    public static void headerValidation(HttpExchange httpExchange) throws IOException {

            if (!httpExchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                badRequest(httpExchange, "not found: Content-Type Header");
                return;
            }

            if (httpExchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).isEmpty()) {
                badRequest(httpExchange, "invalid: Content-Type Header");
                return;
            }

            if (!httpExchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                badRequest(httpExchange, "accept Content-Type: application/json");
                return;
            }
    }

    public static void userDtoValidation(HttpExchange exchange, UserDto userDto) throws IOException {

        if (userDto == null) {
            badRequest(exchange, "parse failed");
            return;
        }

        if (userDto.getId() == 0) {
            badRequest(exchange, "not found: user.id");
            return;
        }

        if (userDto.getUsername() == null) {
            badRequest(exchange, "not found: user.username");
            return;
        }

        if (userDto.getPassword() == null) {
            badRequest(exchange, "not found: user.password");
            return;
        }

        if (userDto.getEmail() == null) {
            badRequest(exchange, "not found: user.email");
            return;
        }

    }

    public static void queryValidation(HttpExchange exchange, Map<String, List<String>> queryStrings) throws IOException {
        if (!queryStrings.containsKey("id")) {
            badRequest(exchange, "not found: query.id");
            System.out.println("오류 테스트 queryValidated");
            return;
        }
        if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) {
            badRequest(exchange, "invalid: query.id");
            System.out.println("오류 테스트 queryValidated2");
            return;
        }
    }

    public static void internalServerError(HttpExchange exchange, String message) {
        try {
            byte[] bytesOfBody = Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_INTERNAL_SERVER_ERROR, "Internal Server Error", message)).getBytes(StandardCharsets.UTF_8);

            Headers headers = exchange.getResponseHeaders();
            headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
            exchange.sendResponseHeaders(HTTP_INTERNAL_SERVER_ERROR, bytesOfBody.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(bytesOfBody);
            outputStream.flush();
        } catch (Exception e) {
            log.error("internal server process in unknown error", e);
        }
    }
}
