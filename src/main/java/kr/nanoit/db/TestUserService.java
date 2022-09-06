package kr.nanoit.db;

import kr.nanoit.domain.UserDto;
import kr.nanoit.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class TestUserService implements UserService {

    private final Map<Integer, UserDto> testUsers;

    public TestUserService() {
        this.testUsers = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            UserDto userDto = new UserDto();
            userDto.setId("test"+ i);
            userDto.setUsername("TESTUSER_" + i);
            userDto.setPassword("TESTPASSWORD_" + i);
            userDto.setEmail("testemail" + i + "@test.com");
            testUsers.put(i, userDto);
        }
    }

    @Override
    public UserDto getUser(int userId) {
        if (testUsers.containsKey(userId)) {
            return testUsers.get(userId);
        } else {
            throw new UserNotFoundException("not found: user.id=" + userId);
        }
    }
}
