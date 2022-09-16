package kr.nanoit.exception;

import java.sql.SQLException;

public class CreateUserFailedException extends SQLException {

    public CreateUserFailedException(String reason) {
        super(reason);
    }
}
