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
    private String deletedAt;
    private String content;
}

////////////////////////////////
// CREATE TABLE todo (
//    id        BIGSERIAL PRIMARY KEY,
//    createdAt TIMESTAMP,
//    deletedAt TIMESTAMP,
//    content   VARCHAR(512)
// )
////////////////////////////////