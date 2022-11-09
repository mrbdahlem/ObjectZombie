package run.mycode.zombieland;

import java.awt.*;

public class Bucket extends Actor {

    private static final Image sprite = ImageLoader.loadResourceImage("/images/bucket.png");

    public Bucket(int xPos, int yPos, World world) {
        super(xPos, yPos, world);
        setImage(sprite);
    }

    /**
     * Buckets don't normally do much.
     */
    @Override
    public void act() { }

    public boolean push(Dir dir) {
        boolean canPush = true;

        int newx = getX() + dir.dx();
        int newy = getY() + dir.dy();

        if (newx < 0 || newx > getWorld().getWidth()
                || newy < 0 || newy > getWorld().getHeight()) {
            canPush = false;
        }

        for (Actor a : getWorld().getActorsAt(newx, newy)) {
            if (a instanceof Wall || a instanceof Bucket) {
                canPush = false;
            }
            else if (a instanceof Fire) {
                ((Fire)a).extinguish();
            }
        }

        if (canPush) {
            setPosition(newx, newy);
        }

        return canPush;
    }
}
