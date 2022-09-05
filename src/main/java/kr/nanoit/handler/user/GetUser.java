package kr.nanoit.handler.user;

import static kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8;
import static kr.nanoit.extension.Variable.CHARSET;
import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.extension.Variable.STATUS_OK;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import kr.nanoit.extension.RelatedBody;
import kr.nanoit.domain.UserService;


/**
 * 클래스 한개는 하나의 일만 한다.
 */

public final class GetUser {

  private static UserService userService;

  private GetUser() {
  }

  public static void handle(HttpExchange exchange) {
    try {
      // print
      InputStream inputStream = exchange.getRequestBody();
      System.out.println(RelatedBody.parseBody(new BufferedReader(new InputStreamReader(inputStream, CHARSET))));

      // response
      Headers headers = exchange.getResponseHeaders();
      headers.add(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
      exchange.sendResponseHeaders(STATUS_OK, 0);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(exchange.getResponseBody(), CHARSET);
      outputStreamWriter.write(userService.getUSer());
      outputStreamWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }
}

//  public JsonArray getUser() {
//    UserService userService = new UserService();
//    return userService.getUser().stream().map(u ->
//      Json.create
//    )
//  }
//}
