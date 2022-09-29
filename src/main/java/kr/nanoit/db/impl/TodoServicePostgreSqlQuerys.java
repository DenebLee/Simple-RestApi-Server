package kr.nanoit.db.impl;

import java.sql.Timestamp;

public final class TodoServicePostgreSqlQuerys {

    public final static String createTodoTable = "CREATE TABLE IF NOT EXISTS todo " +
            "(id BIGSERIAL NOT NULL, " +
            " createAt TIMESTAMP, " +
            " modifiedAt TIMESTAMP, " +
            " content VARCHAR(512), " +
            " writer VARCHAR(64) NOT NULL, " +
            " PRIMARY KEY ( id ))";

    private TodoServicePostgreSqlQuerys() {
    }

    public static String insertTodo(Timestamp createAt, String content,String writer) {
        return "INSERT INTO todo (createAt, content, writer) VALUES ( '" + createAt + "' ,'" + content + "' , '" + writer + "') ";
    }

    public static String selectTodo(long id) {
        return "SELECT * FROM todo WHERE id = '" + id + "' ";
    }

    public static String deleteTodo(long id) {
        return "DELETE FROM todo WHERE id = '" + id + "'";
    }

    public static String updateTodo(Timestamp modifiedAt, String column, String value, long id) {
        return "UPDATE todo SET modifiedAt = '" + modifiedAt + "', " + column + " = '" + value + "' WHERE id = '" + id + "'";
    }
    public static String containsById(long todoId) {
        return "SELECT COUNT(*) AS COUNT FROM todo WHERE id = " + todoId;
    }
}
