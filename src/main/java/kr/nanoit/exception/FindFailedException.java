package kr.nanoit.exception;

public class FindFailedException extends Exception {

    private String reason;

    public FindFailedException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
