package kr.nanoit.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.TestUserService;
import kr.nanoit.object.dto.HttpResponse;
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
import static kr.nanoit.utils.HandlerUtil.badRequest;
import static kr.nanoit.utils.HandlerUtil.notFound;

@Slf4j
public class Validation {

    public static boolean isInvalid(HttpExchange exchange, Map<String, List<String>> queryStrings) throws IOException, JSONException {
        for (Map.Entry<String, List<String>> stringListEntry : queryStrings.entrySet()) {
            log.info("KEY={} VALUES={}", stringListEntry.getKey(), stringListEntry.getValue().stream().map(value -> "[" + value + "]").collect(Collectors.joining(",")));
        }
        if (!queryStrings.containsKey("id")) { // ID 가 없을때
            notFound(exchange, "not found: query.id");
            return true;
        }

        if (queryStrings.containsKey("id") && queryStrings.get("id").size() != 1) { // ID 가 있고 id size가 1이 아닐때
            badRequest(exchange, "invalid: query.id");
            return true;
        }
        return false;
    }

    public static void internalServerError(HttpExchange exchange, String message) { // 내부 서버 오류
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

    public static boolean duplicateValue(HttpExchange exchange, int userId, int httpCode) throws IOException {
            switch (httpCode) {

                // 1 = Delete 2 = get 3 = patch 4 = post
                case 1:
                case 2:
                case 3:
                    if (!TestUserService.testUsers.containsKey(userId)) {
                        notFound(exchange, "UserId not Found");
                        return true;
                    }break;
                case 4:
                    if (TestUserService.testUsers.containsKey(userId)) {
                        badRequest(exchange, "UserId is Exist");
                        return true;
                    }
                    break;
            }
        return false;
    }

}
