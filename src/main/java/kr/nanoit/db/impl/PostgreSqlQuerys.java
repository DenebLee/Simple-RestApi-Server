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

    public static String insertUser(long id, String username, String password, String email) {
        return "INSERTINTO users (username, password, email) VALUES ( '" + username + "', '" + password + "', '" + email + "') ";
    }
}
