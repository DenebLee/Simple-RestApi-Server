package kr.nanoit.db;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.entity.UserEntity;

import java.io.IOException;

public interface UserService {
    static UserService createTest() {
        return new TestUserService();
    }


    boolean isDuplication(HttpExchange exchange, int userId) throws IOException;

    UserEntity save(UserEntity userDto);

    UserEntity findById(int userId);

    boolean deleteById(int userId);

    UserEntity update(UserEntity userEntity);

}
