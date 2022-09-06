package kr.nanoit.db;

import kr.nanoit.domain.UserDto;

public interface UserService {
    static UserService createTest() {
        return new TestUserService();
    }

    UserDto getUser(int userId); // getUser는 userId를 통해 user를 반환
}
