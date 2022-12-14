package kr.nanoit.exception;

public class DeleteException extends Exception {

    private String reason;

    public DeleteException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
