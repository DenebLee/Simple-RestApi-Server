package kr.nanoit.db;

import com.sun.net.httpserver.HttpExchange;
import kr.nanoit.object.entity.UserEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static kr.nanoit.utils.HandlerUtil.*;

public class TestUserService implements UserService {

    private final Map<Integer, UserEntity> testUsers;

    public TestUserService() {
        this.testUsers = new HashMap<>();
        int max = 0;
        for (int i = 1; i <= 100; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setUsername("TESTUSER_" + i);
            userEntity.setPassword("TESTPASSWORD_" + i);
            userEntity.setEmail("testemail" + i + "@test.com");
            testUsers.put(i, userEntity);
            if (i > max) {
                max = i;
            }
        }
    }

    @Override
    public UserEntity findById(int userId) {
            return testUsers.get(userId);
    }

    @Override
    public boolean deleteById(int userId) {
        if (testUsers.containsKey(userId)) {
            testUsers.remove(userId);
            return false;
        }
        return true;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return null;
    }

    @Override
    public boolean isDuplication(HttpExchange exchange, int userId) throws IOException {
        if (!testUsers.containsKey(userId)) {
            badRequest(exchange, "userId is not Exist");
            return true;
        }
        return false;
    }

    @Override
    public UserEntity save(UserEntity userDto) {
        return null;
    }
}
