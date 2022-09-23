package kr.nanoit.handler.user;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;
import static kr.nanoit.utils.GlobalVariable.HEADER_CONTENT_TYPE;

@Slf4j
public final class PostUser {
    private final UserService userService;

    public PostUser(UserService userService) {
        this.userService = userService;
    }


    public void handle(HttpExchange exchange) {
        try {
            // debugging
//            ExchangeRawPrinter.print(exchange);

            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                badRequest(exchange, "not found: Content-Type Header");
                return;
            }

            if (exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).isEmpty()) {
                badRequest(exchange, "invalid: Content-Type Header");
                return;
            }

            if (!exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                badRequest(exchange, "accept Content-Type: application/json");
                return;
            }

            String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody(), Charsets.UTF_8));
            UserDto userDto = getRead(body);
            if (userDto == null) {
                badRequest(exchange, "parse failed");
                return;
            }

            if (userDto.getUsername() == null) {
                badRequest(exchange, "not found: user.username");
                return;
            }

            if (userDto.getPassword() == null) {
                badRequest(exchange, "not found: user.password");
                return;
            }

            if (userDto.getEmail() == null) {
                badRequest(exchange, "not found: user.email");
                return;
            }

            UserEntity userEntity = userService.save(userDto.toEntity());
            System.out.println(userDto);
            System.out.println(userEntity);

            responseOk(exchange, Mapper.writePretty(userEntity.toDto()).getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            log.error("POST /user handler error occurred", e);
            internalServerError(exchange, "Unknown Error");
        } finally {
            exchange.close();
        }
    }

    private UserDto getRead(String body) {
        try {
            return Mapper.read(body, UserDto.class);
        } catch (Exception e) {
            log.error("Json Read error", e);
            return null;
        }
    }
}
