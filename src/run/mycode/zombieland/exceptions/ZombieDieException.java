package run.mycode.zombieland.exceptions;

public class ZombieDieException extends RuntimeException {
    public ZombieDieException(String message) {
        super(message);
    }

    public ZombieDieException(String message, Throwable cause) {
        super(message, cause);
    }
}
