package kr.nanoit.object.dto;

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
}
