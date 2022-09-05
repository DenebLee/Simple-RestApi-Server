package kr.nanoit.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String create(User user) {
//        return userRepository.create(user);
        return "";
    }

    public String getUSer() {
        return "";
    }
}