package kr.nanoit.exception;

public class GetException extends Exception {

    private String reason;

    public GetException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
