package run.mycode.zombieland;

import java.util.List;
import java.awt.*;

public class Fire extends Actor{
    private boolean isBurning = true;

    private static final Image[] sprite = new Image[12];
    private static final Image[] smoke = new Image[12];

    static {
        for (int i = 0; i < sprite.length; i++) {
            sprite[i] = ImageLoader.loadResourceImage("/images/Fire-" + i + ".png");
        }
        for (int i = 0; i < smoke.length; i++) {
            smoke[i] = ImageLoader.loadResourceImage("/images/Smoke-" + i + ".png");
        }
    }

    public Fire(int xPos, int yPos, World world) {
        super(xPos, yPos, world);
        setFrameOffset((int)(Math.random() * sprite.length));
    }

    /**
     * Burn.
     */
    @Override
    public void act() {
        if (isBurning) {
            List<Actor> actors = getWorld().getActorsAt(getX(), getY());
            for (Actor a : actors) {
                if (a instanceof Zombie.ZombieActor) {
                    Zombie z = ((Zombie.ZombieActor) a).getZombie();
                    z.die();
                    extinguish();
                }
            }
        }
        else {
            if (getFrameNo(smoke.length + 1) == smoke.length) {
                getWorld().remove(this);
            }
        }
    }

    public void extinguish() {
        isBurning = false;
        resetFrameNo();
    }

    @Override
    Image getImage() {
        if (isBurning) {
            return sprite[getFrameNo(sprite.length)];
        }
        else {
            return smoke[getFrameNo(smoke.length)];
        }
    }
}
