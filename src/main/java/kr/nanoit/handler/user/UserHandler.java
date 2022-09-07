package kr.nanoit.handler.user;

import static kr.nanoit.extension.Variable.APPLICATION_JSON_CHARSET_UTF_8;
import static kr.nanoit.extension.Variable.HEADER_CONTENT_TYPE;
import static kr.nanoit.extension.Variable.METHOD_DELETE;
import static kr.nanoit.extension.Variable.METHOD_GET;
import static kr.nanoit.extension.Variable.METHOD_PATCH;
import static kr.nanoit.extension.Variable.METHOD_POST;
import static kr.nanoit.utils.HandlerUtil.badRequest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kr.nanoit.db.TestUserService;
import kr.nanoit.db.UserService;

import java.io.IOException;


public class UserHandler implements HttpHandler {

  private final UserService userService;
  private final GetUser getUser;
  private final DeleteUser deleteUser;
  private final PostUser postUser;

  private final PatchUser patchUser;

  public UserHandler() {
    this.userService = new TestUserService();
//    this.userService = new DbUserService();
    this.getUser = new GetUser(userService);
    this.deleteUser = new DeleteUser(userService);
    this.postUser = new PostUser(userService);
    this.patchUser = new PatchUser(userService);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    if (METHOD_POST.equals(method)) {
      postUser.handle(exchange);
    } else if (METHOD_GET.equals(method)) {
      getUser.handle(exchange);
    } else if (METHOD_PATCH.equals(method)) {
      patchUser.handle(exchange);
    } else if (METHOD_DELETE.equals(method)) {
      deleteUser.handle(exchange);
    } else {
      badRequest(exchange,"handler error");
    }
  }
}






