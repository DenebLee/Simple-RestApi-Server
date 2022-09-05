package kr.nanoit.handler.user;

import static kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8;
import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.extension.Variable.METHOD_DELETE;
import static kr.nanoit.extension.Variable.METHOD_GET;
import static kr.nanoit.extension.Variable.METHOD_PATCH;
import static kr.nanoit.extension.Variable.METHOD_POST;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 * 클래스 한개는 하나의 일만 한다.
 */
public class UserHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) {
    String method = exchange.getRequestMethod();
    if (METHOD_POST.equals(method)) {
      PostUser.handle(exchange);
    } else if (METHOD_GET.equals(method)) {
      GetUser.handle(exchange);
    } else if (METHOD_PATCH.equals(method)) {
      PatchUser.handle(exchange);
    } else if (METHOD_DELETE.equals(method)) {
      DeleteUser.handle(exchange);
    } else {
      badRequest(exchange);
    }
  }

  private static void badRequest(HttpExchange exchange) {
    try {
      Headers headers = exchange.getResponseHeaders();
      headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
      exchange.sendResponseHeaders(405, -1);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}






