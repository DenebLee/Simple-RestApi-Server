package kr.nanoit.object.dto;

import kr.nanoit.object.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoDto {
    private long todoId;
    private String createdAt;
    private String deletedAt;
    private String content;

    public UserEntity toEntity() {
        return new UserEntity(todoId, createdAt, deletedAt, content);
    }
}
