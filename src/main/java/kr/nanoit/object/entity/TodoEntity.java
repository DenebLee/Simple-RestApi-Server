package kr.nanoit.object.entity;

import kr.nanoit.object.dto.TodoDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoEntity {
    private long todoId;
    private String createdAt;
    private String modifiedAt;
    private String content;
    private String writer;

    public TodoDto toDto() {
        return new TodoDto(todoId, createdAt, modifiedAt, content, writer);
    }
}