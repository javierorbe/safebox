package deusto.safebox.common.net.packet;

public class ErrorPacket extends Packet {

    private static final long serialVersionUID = 2244582856053708269L;

    private final ErrorType errorType;

    private ErrorPacket(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        return getClass().getName() + " (" + errorType.toString() + ")";
    }

    public enum ErrorType {
        UNKNOWN_ERROR,
        EMAIL_ALREADY_IN_USE,
        INVALID_LOGIN,
        ;

        public ErrorPacket get() {
            return new ErrorPacket(this);
        }
    }
}
