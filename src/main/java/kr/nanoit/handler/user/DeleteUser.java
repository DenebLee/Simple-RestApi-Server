package kr.nanoit.handler.user;

import static kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8;
import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.extension.Variable.HTTP_OK;
import static kr.nanoit.utils.HandlerUtil.print;
import static kr.nanoit.utils.Validation.*;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.object.entity.UserEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public final class DeleteUser {

    private final UserService userService;

    DeleteUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            print(exchange);

            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());
            if (isInvalid(exchange, queryStrings)) return;

            Headers headers = exchange.getResponseHeaders();
            headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
            exchange.sendResponseHeaders(HTTP_OK, 0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }


    private static String parseBody(BufferedReader bufferedReader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            builder.append(inputLine);
        }
        return builder.toString();
    }
}
