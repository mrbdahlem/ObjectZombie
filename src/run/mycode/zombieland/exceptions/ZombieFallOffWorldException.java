package run.mycode.zombieland.exceptions;

/**
 * The zombie tried to move outside the bounds of the world
 */
public class ZombieFallOffWorldException extends RuntimeException {

    public ZombieFallOffWorldException() {
        super("Zombie fall off world.");
    }

    public ZombieFallOffWorldException(Throwable cause) {
        super("Zombie fall off world.", cause);
    }
}
