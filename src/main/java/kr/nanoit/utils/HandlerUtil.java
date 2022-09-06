package kr.nanoit.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.extension.RelatedBody;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static kr.nanoit.extension.Variable.*;

public class HandlerUtil
{

    public static void print(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        System.out.println(RelatedBody.parseBody(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))));
    }

    public static void responseOk(HttpExchange exchange, byte[] rawBytes) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);

        exchange.sendResponseHeaders(HTTP_OK, rawBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(rawBytes);
        outputStream.flush();
    }

}
