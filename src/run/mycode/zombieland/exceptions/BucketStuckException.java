package run.mycode.zombieland.exceptions;

/**
 * The zombie tried to move outside the bounds of the world
 */
public class BucketStuckException extends ZombieDieException {
    private static final String message = "Bucket not move";
    public BucketStuckException() {
        super(message);
    }

    public BucketStuckException(Throwable cause) {
        super(message, cause);
    }
}
