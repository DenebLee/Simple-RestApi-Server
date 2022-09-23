package kr.nanoit.handler.common;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.dto.HttpResponseDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class Validation {
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
