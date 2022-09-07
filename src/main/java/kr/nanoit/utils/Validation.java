package kr.nanoit.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.dto.HttpResponse;
import kr.nanoit.object.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.nanoit.extension.Variable.*;
import static kr.nanoit.extension.Variable.HTTP_BAD_REQUEST;
import static kr.nanoit.utils.HandlerUtil.badRequest;

@Slf4j
public class Validation {
    public static boolean isInvalid(HttpExchange exchange, Map<String, List<String>> queryStrings) throws IOException, JSONException {
        for (Map.Entry<String, List<String>> stringListEntry : queryStrings.entrySet()) {
            log.info("KEY={} VALUES={}", stringListEntry.getKey(), stringListEntry.getValue().stream().map(value -> "[" + value + "]").collect(Collectors.joining(",")));
        }
        if (!queryStrings.containsKey("id")) {
            badRequest(exchange, "not found: query.id");
            return true;
        }
        if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) {
            badRequest(exchange, "invalid: query.id");
            return true;
        }
        return false;
    }
    public static void internalServerError(HttpExchange exchange, String message) {
        try {
            byte[] bytesOfBody = Mapper.writePretty(new HttpResponse(OffsetDateTime.now().toString(), HTTP_INTERNAL_SERVER_ERROR, "Internal Server Error", message)).getBytes(StandardCharsets.UTF_8);

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
    public static boolean IsNotContainsJson(HttpExchange exchange) throws IOException {
        if (exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE) && exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).size() > 0) {
            if (exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                return false;
            }
        }
        badRequest(exchange, "not found: Content-Type Header");
        return true;
    }
    public static boolean isNull(HttpExchange exchange, UserDto userDto) throws IOException {
        if (userDto == null) {
            badRequest(exchange, "parse failed");
            return true;
        }
        return false;
    }
}
