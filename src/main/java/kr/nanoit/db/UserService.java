package kr.nanoit.db;

import kr.nanoit.object.entity.UserEntity;

public interface UserService {
    static UserService createTest() {
        return new TestUserService();
    }


    UserEntity save(UserEntity userDto);

    UserEntity findById(int userId);

    boolean deleteById(int userId);

    UserEntity update(UserEntity userEntity);
}
