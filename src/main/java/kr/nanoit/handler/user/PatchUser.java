package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;

public final class PatchUser {

    private final UserService userService;

    public PatchUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }
}
