package kr.nanoit.exception;

public class PostException extends Exception {

    private final String reason;

    public PostException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
