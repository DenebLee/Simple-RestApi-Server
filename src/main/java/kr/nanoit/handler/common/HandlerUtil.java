package kr.nanoit.handler.common;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.dto.HttpResponseDto;
import kr.nanoit.utils.GlobalVariable;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class HandlerUtil {

    public static void responseOk(HttpExchange exchange, byte[] rawBytes) throws IOException {
        setHeader(exchange);
        outPutStream(exchange, rawBytes, HTTP_OK);
    }


    public static void badRequest(HttpExchange exchange, String message) throws IOException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_BAD_REQUEST, "BadRequest", message)).getBytes(StandardCharsets.UTF_8);
        setHeader(exchange);
        outPutStream(exchange, bytesOfBody, HTTP_BAD_REQUEST);
    }

    public static void notFound(HttpExchange exchange, String message) throws IOException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponseDto(OffsetDateTime.now().toString(), HTTP_NOT_FOUND, "BadRequest", message)).getBytes(StandardCharsets.UTF_8);
        setHeader(exchange);
        outPutStream(exchange, bytesOfBody, HTTP_NOT_FOUND);
    }

    private static void outPutStream(HttpExchange exchange, byte[] text, int HttpCode) throws IOException {
        exchange.sendResponseHeaders(HttpCode, text.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(text);
        outputStream.flush();
    }

    private static void setHeader(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add(GlobalVariable.HEADER_CONTENT_TYPE, GlobalVariable.APPLICATION_JSON_CHARSET_UTF_8);
    }
}
