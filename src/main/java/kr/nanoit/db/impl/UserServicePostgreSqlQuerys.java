package kr.nanoit.db.impl;

public final class UserServicePostgreSqlQuerys {

    public final static String createUserTable = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGSERIAL NOT NULL, " +
            " username VARCHAR(255) , " +
            " password VARCHAR(255) , " +
            " email VARCHAR(255) UNIQUE, " +
            " PRIMARY KEY ( id ))";

    private UserServicePostgreSqlQuerys() {
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

    //  재 사용 할 수 있도록 칼럼과 value 에 파라미터 삽입 할 수 있게 sql 문 작성
    public static String updateUser(String column, String value, long id) {
        return "UPDATE users set " + column + " = '" + value + "' WHERE id = '" + id + "'";
    }

    public static String containsById(long id) {
        return "SELECT COUNT(*) AS COUNT FROM users WHERE id = " + id;
    }

    public static String columnDuplicateValue(String username, String email) {
        return "SELECT COUNT(*) AS COUNT FROM users WHERE username = '" + username + "'AND'" + email + "' ";
    }
}
