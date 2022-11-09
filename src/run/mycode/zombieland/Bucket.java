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
}
