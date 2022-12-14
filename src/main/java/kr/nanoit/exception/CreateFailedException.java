package kr.nanoit.exception;

import java.sql.SQLException;

public class CreateFailedException extends SQLException {

    private String reason;
    public CreateFailedException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
