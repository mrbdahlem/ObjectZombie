package run.mycode.zombieland;

import java.awt.*;

public class Wall extends Actor {

    private static final Image sprite = ImageLoader.loadResourceImage("/images/brick.jpg");

    public Wall(int xPos, int yPos, World world) {
        super(xPos, yPos, world);
        setImage(sprite);
    }
    /**
     * Walls don't do much.
     */
    @Override
    public void act() { }
}
