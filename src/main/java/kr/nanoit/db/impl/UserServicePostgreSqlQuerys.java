package kr.nanoit.db.impl;

import kr.nanoit.exception.TodoBadRequestException;
import kr.nanoit.object.entity.UserEntity;

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

    public static String updateUser(UserEntity userEntity) throws Exception {
        if (userEntity.getUsername() != null) {
            return("UPDATE users SET username = '" + userEntity.getUsername() + "' WHERE id = '" + userEntity.getId() + "'");
        }
        if(userEntity.getPassword() != null){
            return("UPDATE users SET password = '" + userEntity.getPassword() + "' WHERE id = '" + userEntity.getId() + "'");
        }
        if(userEntity.getEmail() != null){
            return("UPDATE users SET email = '" + userEntity.getEmail() + "' WHERE id = '" + userEntity.getId() + "'");
        }
//        return "UPDATE users SET username = '" + userEntity.getUsername() + "', password = '" + userEntity.getPassword() + "', email = '" + userEntity.getEmail() + "' WHERE id = '" + userEntity.getId() + "'";
        throw new TodoBadRequestException("Server Error");
    }

    public static String containsById(long id) {
        return "SELECT COUNT(*) AS COUNT FROM users WHERE id = " + id;
    }
}
