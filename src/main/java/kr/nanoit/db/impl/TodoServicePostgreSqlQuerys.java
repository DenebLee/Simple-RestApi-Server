package kr.nanoit.db.impl;

import java.sql.Timestamp;

public final class TodoServicePostgreSqlQuerys {

    public final static String createTodoTable = "CREATE TABLE IF NOT EXISTS todo " +
            "(id BIGSERIAL NOT NULL, " +
            " createAt TIMESTAMP, " +
            " deleteAt TIMESTAMP, " +
            " content VARCHAR(512), " +
            " PRIMARY KEY ( id ))";

    private TodoServicePostgreSqlQuerys() {
    }

    public static String insertTodo(Timestamp createAt, String content) {
        return "INSERT INTO todo (createAt, content) VALUES ( '" + createAt + "' ,'" + content + "') ";
    }

    public static String selectTodo(long id) {
        return "SELECT * FROM todo WHERE id = '" + id + "' ";
    }

    public static String deleteTodo(long id) {
        return "DELETE FROM todo WHERE id = '" + id + "'";
    }

    public static String updateTodo(long id, Timestamp createAt, Timestamp deleteAt, String content) {
        return "UPDATE todo SET createAt = '" + createAt + "', deleteAt = '" + deleteAt + "', content = '" + content + "' WHERE id = '" + id + "'";


    }
}
