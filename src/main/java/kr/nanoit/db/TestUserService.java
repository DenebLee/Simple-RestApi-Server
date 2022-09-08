package kr.nanoit.db;

import kr.nanoit.exceptions.UserNotFoundException;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.HandlerUtil;

import java.util.HashMap;
import java.util.Map;

public class TestUserService implements UserService {

    public static  Map<Integer, UserEntity> testUsers;

    public TestUserService() {
        testUsers = new HashMap<>();
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
       return testUsers.get(userId);
    }

    @Override
    public boolean deleteById(int userId) {
            testUsers.remove(userId);
            return true;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return null;
    }

    @Override
    public boolean containsById(int id) {
        return testUsers.containsKey(id);
    }

    @Override
    public UserEntity save(UserEntity userDto) {
        int max = 0;
        // 5, 8, 1, 3
        for (Map.Entry<Integer, UserEntity> entry : testUsers.entrySet()) {
            int current = entry.getKey();
            if (current > max){
                max = current;
            }

        }
        userDto.setId(max);
        return testUsers.put(max, userDto);
    }
}
