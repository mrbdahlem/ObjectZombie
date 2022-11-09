package run.mycode.zombieland.exceptions;

/**
 * The zombie tried to move outside the bounds of the world
 */
public class ZombieHitWallException extends ZombieDieException {
    private static final String message = "Zombie hit wall";
    public ZombieHitWallException() {
        super(message);
    }

    public ZombieHitWallException(Throwable cause) {
        super(message, cause);
    }
}
