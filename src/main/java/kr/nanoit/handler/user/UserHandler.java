package kr.nanoit.handler.user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kr.nanoit.db.impl.userservice.UserService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static kr.nanoit.utils.GlobalVariable.*;

@Slf4j
public class UserHandler implements HttpHandler {

    private final UserService userService;
    private final GetUser getUser;
    private final DeleteUser deleteUser;
    private final PostUser postUser;
    private final PatchUser patchUser;

    public UserHandler(UserService userService) {
        this.userService = userService;
//    this.userService = new DbUserService();
        this.getUser = new GetUser(userService);
        this.deleteUser = new DeleteUser(userService);
        this.postUser = new PostUser(userService);
        this.patchUser = new PatchUser(userService);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case METHOD_GET:
                    getUser.handle(exchange);
                    break;
                case METHOD_POST:
                    postUser.handle(exchange);
                    break;
                case METHOD_DELETE:
                    deleteUser.handle(exchange);
                    break;
                case METHOD_PATCH:
                    patchUser.handle(exchange);
                    break;
                default:
                    badRequest(exchange);
                    break;
            }
        } catch (Exception e) {
            log.error("handler error", e);
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






