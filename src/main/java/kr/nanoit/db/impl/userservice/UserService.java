package kr.nanoit.db.impl.userservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.exception.UpdateException;
import kr.nanoit.object.entity.UserEntity;

public interface UserService {
    static UserService createTest() {
        return new UserServiceTestImpl();
    }

    static UserService createPostgreSQL(PostgreSqlDbcp dbcp) {
        return new UserServicePostgreSQLImpl(dbcp);
    }

    UserEntity save(UserEntity userEntity) throws CreateFailedException;

    UserEntity findById(long userId) throws FindFailedException;

    boolean deleteById(long userId) throws DeleteException;

    UserEntity update(UserEntity userEntity) throws UpdateException;

    boolean containsById(long id);
}
