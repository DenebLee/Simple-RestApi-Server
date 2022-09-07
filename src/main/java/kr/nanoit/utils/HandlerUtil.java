package kr.nanoit.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.extension.RelatedBody;
import kr.nanoit.object.dto.HttpResponse;
import org.json.JSONException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static kr.nanoit.extension.Variable.*;

public class HandlerUtil {

    public static void print(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        System.out.println(RelatedBody.parseBody(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))));
    }

    public static void responseOk(HttpExchange exchange, byte[] rawBytes) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        exchange.sendResponseHeaders(HTTP_OK, rawBytes.length);
        outPutStream(exchange, rawBytes);
    }

    public static void badRequest(HttpExchange exchange, String message) throws IOException, JSONException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponse(OffsetDateTime.now().toString(), HTTP_BAD_REQUEST, "BadRequest", message)).getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        exchange.sendResponseHeaders(HTTP_BAD_REQUEST, bytesOfBody.length);
        outPutStream(exchange, bytesOfBody);

    }

    public static void outPutStream(HttpExchange exchange, byte[] text) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(text);
        outputStream.flush();
    }
}
