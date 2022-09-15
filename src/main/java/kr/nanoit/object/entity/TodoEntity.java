package kr.nanoit.object.entity;

import kr.nanoit.object.dto.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoEntity {
    private long todoId;
    private String createdAt;
    private String deletedAt;
    private String content;

    public UserDto toDto() {
        return new UserDto(todoId, createdAt, deletedAt, content);
    }
}
