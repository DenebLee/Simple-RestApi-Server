package kr.nanoit.handler.user;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.UserService;
import kr.nanoit.extension.QueryParsing;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static kr.nanoit.utils.HandlerUtil.*;
import static kr.nanoit.utils.Validation.*;

@Slf4j
public class GetUser {

    private final UserService userService;

    public GetUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) {
        try {
            Map<String, List<String>> queryStrings = QueryParsing.splitQuery(exchange.getRequestURI().getRawQuery());
            if (isInvalid(exchange, queryStrings)) return;

            int userId = Integer.parseInt(queryStrings.get("id").get(0));
            System.out.println( Mapper.read("json",UserDto.class));
            if(userService.isDuplication(exchange,userId))return;
            UserEntity userEntity = userService.findById(userId);
            UserDto userDto = userEntity.toDto();

            if (isNull(exchange, userDto)) return;

            responseOk(exchange, Mapper.writePretty(userDto).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("get handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }
}
