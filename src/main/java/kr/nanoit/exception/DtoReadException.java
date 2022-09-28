package kr.nanoit.exception;

public class DtoReadException extends Exception {
    private final String reason;

    public DtoReadException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
