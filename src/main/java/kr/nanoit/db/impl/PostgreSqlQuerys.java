package kr.nanoit.db.impl;

public final class PostgreSqlQuerys {

    public final static String createUserTable = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGSERIAL NOT NULL, " +
            " username VARCHAR(255), " +
            " password VARCHAR(255), " +
            " email VARCHAR(255), " +
            " PRIMARY KEY ( id ))";

    private PostgreSqlQuerys() {
    }

    public static String insertUser(String username, String password, String email) {
        return "INSERT INTO users (username, password, email) VALUES ( '" + username + "', '" + password + "', '" + email + "') ";
    }

    public static String selectUser(long id) {
        return "SELECT * FROM users WHERE id = '" + id + "' ";
    }

    public static String deleteUser(long id) {
        return "DELETE FROM users WHERE id = '" + id + "'";
    }

    public static String updateUser(long id, String username, String password, String email) {
        return "UPDATE users SET username = '" + username + "', password = '" + password + "', email = '" + email + "' WHERE id = '" + id + "'";


    }
}
