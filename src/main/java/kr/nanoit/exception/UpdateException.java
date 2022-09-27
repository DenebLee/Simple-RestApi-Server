package kr.nanoit.exception;

public class UpdateException extends Exception {

    private String reason;

    public UpdateException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
