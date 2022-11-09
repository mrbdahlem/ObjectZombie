package run.mycode.zombieland.exceptions;

/**
 * The zombie tried to move outside the bounds of the world
 */
public class ZombieFallOffWorldException extends ZombieDieException {
    private static final String message = "Zombie fall off world";

    public ZombieFallOffWorldException() { super(message); }

    public ZombieFallOffWorldException(Throwable cause) { super(message, cause); }
}
