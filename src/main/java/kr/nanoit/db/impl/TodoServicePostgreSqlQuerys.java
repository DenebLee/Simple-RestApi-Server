package kr.nanoit.db.impl;

import java.sql.Timestamp;

public final class TodoServicePostgreSqlQuerys {

    public final static String createTodoTable = "CREATE TABLE IF NOT EXISTS todo " +
            "(id BIGSERIAL NOT NULL, " +
            " createAt TIMESTAMP, " +
            " modifiedAt TIMESTAMP, " +
            " content VARCHAR(512), " +
            " completed BOOLEAN NOT NULL, " +
            " PRIMARY KEY ( id ))";

    private TodoServicePostgreSqlQuerys() {
    }

    public static String insertTodo(Timestamp createAt, String content,boolean completed) {
        return "INSERT INTO todo (createAt, content, completed) VALUES ( '" + createAt + "' ,'" + content + "' , '" + completed + "') ";
    }

    public static String selectTodo(long id) {
        return "SELECT * FROM todo WHERE id = '" + id + "' ";
    }

    public static String deleteTodo(long id) {
        return "DELETE FROM todo WHERE id = '" + id + "'";
    }

    public static String updateTodo(long id, Timestamp modifiedAt, String content, boolean completed) {
        return "UPDATE todo SET modifiedAt = '" + modifiedAt + "', content = '" + content + "' , completed = '" + completed + "' WHERE id = '" + id + "'";
    }
}
