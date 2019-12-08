package deusto.safebox.common.net.packet;

/** Packet sent to notify that some type of error has occurred. */
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
        return getClass().getSimpleName() + " (" + errorType.toString() + ")";
    }

    public enum ErrorType {
        UNKNOWN_ERROR,
        EMAIL_ALREADY_IN_USE,
        INVALID_LOGIN,
        REGISTER_ERROR,
        SAVE_DATA_ERROR,
        ;

        public ErrorPacket get() {
            return new ErrorPacket(this);
        }
    }
}
