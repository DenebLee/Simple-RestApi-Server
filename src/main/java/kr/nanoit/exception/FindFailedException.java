package kr.nanoit.exception;

import java.sql.SQLException;

public class FindFailedException extends SQLException {

    public FindFailedException(String message) {
        super(message);
    }
}