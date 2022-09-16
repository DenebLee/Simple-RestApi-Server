package kr.nanoit.exception;

import java.sql.SQLException;

public class DataBaseInternalError extends SQLException {
    public DataBaseInternalError(String reason) {
        super(reason);
    }
}
