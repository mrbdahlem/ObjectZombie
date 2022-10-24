package run.mycode.zombieland;

import java.util.List;
import java.awt.*;
import java.util.stream.Collectors;

public class ZombieGoal extends Actor {
    private static final Image[] sprite  = new Image[6];

    static {
        for (int i = 0; i < sprite.length; i++) {
            sprite[i] = ImageLoader.loadResourceImage("/images/Goal-" + i + ".png");
        }
    }

    public ZombieGoal(int xPos, int yPos, World world) {
        super(xPos, yPos, world);
    }

    /**
     * Determine if a Zombie is standing on this goal, if so that Zombie has "won"
     */
    @Override
    public void act() {
        List<Zombie> zombies = getWorld().getActorsAt(getX(), getY()).stream()
                .filter(a -> a instanceof Zombie.ZombieActor)
                .map(za -> ((Zombie.ZombieActor) za).getZombie())
                .collect(Collectors.toList());

        for (Zombie z : zombies) {
            if (z.isUndead() && !z.hasWon()) {
                z.win();
            }
        }

    }

    @Override
    Image getImage() {
        return sprite[getFrameNo(sprite.length)];
    }
}
