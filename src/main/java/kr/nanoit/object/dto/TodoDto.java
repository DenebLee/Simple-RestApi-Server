package kr.nanoit.object.dto;

import kr.nanoit.object.entity.TodoEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoDto {
    private long todoId;
    private String createdAt;
    private String modifiedAt;
    private String content;
    private String writer;

    public TodoEntity toEntity() {
        return new TodoEntity(todoId, createdAt, modifiedAt, content, writer);
    }
}
