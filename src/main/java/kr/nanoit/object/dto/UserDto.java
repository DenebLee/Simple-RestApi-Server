package kr.nanoit.object.dto;

import kr.nanoit.object.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private long id;
    private String username;
    private String password;
    private String email;

    public UserEntity toEntity() {
        return new UserEntity(id, username, password, email);
    }
}
