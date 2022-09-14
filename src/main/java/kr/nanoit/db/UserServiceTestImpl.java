package kr.nanoit.db;

import kr.nanoit.object.entity.UserEntity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserServiceTestImpl implements UserService {

    private final AtomicLong userId;
    private final ConcurrentHashMap<Long, UserEntity> users;

    public UserServiceTestImpl() {
        this.userId = new AtomicLong(0);
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public UserEntity findById(long userId) {
        return users.get(userId);
    }

    @Override
    public boolean deleteById(long userId) {
        users.remove(userId);
        return true;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        long key = userEntity.getId();
        users.put(key,userEntity);
        return users.get(key);
    }

    @Override
    public boolean containsById(long id) {
        return users.containsKey(id);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        long key = userId.incrementAndGet();
        userEntity.setId(key);
        users.put(key, userEntity);
        return users.get(key);
    }
}
