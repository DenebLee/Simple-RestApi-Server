package kr.nanoit.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.dto.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static kr.nanoit.extension.Variable.*;

@Slf4j
public class HandlerUtil {

    public static JSONObject requestBodyParsing(HttpExchange exchange) throws IOException, ParseException {
        InputStream requestBody = exchange.getRequestBody();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(requestBody, StandardCharsets.UTF_8));
        return jsonObject;
    }

    public static void responseOk(HttpExchange exchange, byte[] rawBytes) throws IOException {
        setHeader(exchange);
        outPutStream(exchange, rawBytes, HTTP_OK);
    }


    // if문으로 HttpCode(404, 400)에따라 error메시지 다르게 주기
    public static void badRequest(HttpExchange exchange, String message) throws IOException, JSONException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponse(OffsetDateTime.now().toString(), HTTP_BAD_REQUEST, "BadRequest", message)).getBytes(StandardCharsets.UTF_8);
        setHeader(exchange);
        outPutStream(exchange, bytesOfBody, HTTP_BAD_REQUEST);
    }

    public static void notFound(HttpExchange exchange, String message) throws IOException, JSONException {
        byte[] bytesOfBody = Mapper.writePretty(new HttpResponse(OffsetDateTime.now().toString(), HTTP_NOT_FOUND, "BadRequest", message)).getBytes(StandardCharsets.UTF_8);
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
        headers.add(kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE, kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8);
    }
}
