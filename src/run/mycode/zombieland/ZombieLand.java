package run.mycode.zombieland;

import java.awt.*;

public class ZombieLand extends World {

    private final String name;

    private static Image background = ImageLoader.loadResourceImage("/images/zombieland.png");

    public ZombieLand(int width, int height, String name) {
        super(width, height, background.getHeight(null), background);

        this.name = name;
    }

    public String toString() {
        return name;
    }
}
