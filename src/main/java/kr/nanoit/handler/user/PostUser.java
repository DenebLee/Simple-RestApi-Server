package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;

import java.util.List;
import java.util.Map;

import static kr.nanoit.utils.HandlerUtil.print;
import static kr.nanoit.utils.Validation.IsNotContainsJson;


public class PostUser {

    private final UserService userService;

    public PostUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            print(exchange);
            if(IsNotContainsJson(exchange))return;
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());

            int userId = Integer.parseInt(queryStrings.get("id").get(0));
            if(userService.isDuplication(exchange,userId)) return;


            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }
}
