package kr.nanoit.object.entity;

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
    private boolean completed;
}