package kr.nanoit.db;

import kr.nanoit.object.entity.UserEntity;

public interface UserService {
    static UserService createTest() {
        return new UserServiceTestImpl();
    }

    UserEntity save(UserEntity userEntity);

    UserEntity findById(long userId);

    boolean deleteById(long userId);

    UserEntity update(UserEntity userEntity);

    boolean containsById(long id);
}
