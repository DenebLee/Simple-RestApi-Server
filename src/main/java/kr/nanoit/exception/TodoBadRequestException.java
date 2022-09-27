package kr.nanoit.exception;

public class TodoBadRequestException extends Exception {
    private String reason;

    public TodoBadRequestException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
