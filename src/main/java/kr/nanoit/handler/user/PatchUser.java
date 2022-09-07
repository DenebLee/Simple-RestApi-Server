package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import static kr.nanoit.utils.Validation.internalServerError;
import static kr.nanoit.utils.HandlerUtil.print;

@Slf4j
public class PatchUser {

    private final UserService userService;
    public PatchUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            print(exchange);
            System.out.println( Mapper.read("json",UserDto.class));


        } catch (Exception e) {
            log.error("Patch handler error occurred", e);
            internalServerError(exchange, "unknown Error");
        } finally {
            exchange.close();
        }
    }
}
