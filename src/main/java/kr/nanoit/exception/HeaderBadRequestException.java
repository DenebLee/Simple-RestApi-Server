package kr.nanoit.exception;

public class HeaderBadRequestException extends Exception {
    private String reason;

    public HeaderBadRequestException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
