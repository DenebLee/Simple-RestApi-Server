package kr.nanoit.handler.user;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.exception.DtoReadException;
import kr.nanoit.exception.UpdateException;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static kr.nanoit.handler.common.HandlerUtil.badRequest;
import static kr.nanoit.handler.common.HandlerUtil.responseOk;
import static kr.nanoit.handler.common.Validation.internalServerError;
import static kr.nanoit.utils.GlobalVariable.HEADER_CONTENT_TYPE;

@Slf4j
public final class PatchUser {

    private final UserService userService;

    public PatchUser(UserService userService) {
        this.userService = userService;
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (!exchange.getRequestHeaders().containsKey(HEADER_CONTENT_TYPE)) {
                throw new UpdateException("not found: Content-Type Header");
            }

            if (!exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).get(0).equalsIgnoreCase("application/json")) {
                throw new UpdateException("accept Content-Type: application/json");
            }

            String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody(), Charsets.UTF_8));
            UserDto userDto = getRead(body);

            if (userDto == null) {
                throw new DtoReadException("parse failed");
            }

            if (userDto.getId() == 0) {
                throw new DtoReadException("User ID is missing");
            }

            UserEntity userEntity = userService.update(userDto.toEntity());

            if (userEntity == null) {
                internalServerError(exchange, "update query failed");
            }

            responseOk(exchange, Mapper.writePretty(userEntity).getBytes(StandardCharsets.UTF_8));

        } catch (UpdateException | DtoReadException e) {
            badRequest(exchange, e.getMessage());
        } catch (Exception e) {
            log.error("patch handler error occurred", e);
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
