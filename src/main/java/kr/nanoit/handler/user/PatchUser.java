package kr.nanoit.handler.user;

import static kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8;
import static kr.nanoit.extension.Variable.CHARSET;
import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.extension.Variable.HTTP_OK;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import kr.nanoit.extension.RelatedBody;

/**
 * 클래스 한개는 하나의 일만 한다.
 */
public final class PatchUser {

  private PatchUser() {
  }

  public static void handle(HttpExchange exchange) {
    try {
      // print
      InputStream inputStream = exchange.getRequestBody();
      System.out.println(RelatedBody.parseBody(new BufferedReader(new InputStreamReader(inputStream, CHARSET))));

      // response
      Headers headers = exchange.getResponseHeaders();
      headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
      exchange.sendResponseHeaders(HTTP_OK, 0);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }
}
