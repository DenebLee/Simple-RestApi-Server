package kr.nanoit.object.entity;

import kr.nanoit.object.dto.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {

    private long id;
    private String username;
    private String password;
    private String email;

    public UserDto toDto() {
        return new UserDto(id, username, password, email);
    }
}
