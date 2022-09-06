package kr.nanoit.db;

import kr.nanoit.object.dto.UserDto;
import kr.nanoit.exceptions.UserNotFoundException;
import kr.nanoit.object.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

public class TestUserService implements UserService {

    private final Map<Integer, UserEntity> testUsers;

    public TestUserService() {
        this.testUsers = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setUsername("TESTUSER_" + i);
            userEntity.setPassword("TESTPASSWORD_" + i);
            userEntity.setEmail("testemail" + i + "@test.com");
            testUsers.put(i, userEntity);
        }
    }

    @Override
    public UserEntity findById(int userId) {
        if (testUsers.containsKey(userId)) {
            return testUsers.get(userId);
        } else {
            throw new UserNotFoundException("not found: user.id=" + userId);
        }
    }

    @Override
    public boolean deleteById(int userId) {
        return false;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return null;
    }

    @Override
    public UserEntity save(UserEntity userDto) {
        return null;
    }
}
