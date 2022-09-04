package kr.nanoit.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
    public List<UserDto> user = new ArrayList<>();

    public UserService() {
    user.add(new UserDto("test", "123123", "test@test.com"));
    }


    public List<UserDto> getUser(){
        return user;
    }

    public void create(UserDto userDto) {
        user.add(userDto);
    }
}
