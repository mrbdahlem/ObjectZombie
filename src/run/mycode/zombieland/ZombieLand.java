package run.mycode.zombieland;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.util.stream.Collectors;

public class ZombieLand extends World {

    private final String name;

    private static final Image background = ImageLoader.loadResourceImage("/images/zombieland.png");

    private final List<Actor> zombies = new ArrayList<>();

    @SuppressWarnings("unused")
    public ZombieLand(int width, int height, String name) {
        super(width, height, background.getHeight(null), background);

        this.name = name;
    }

    @Override
    public void act() {
        super.act();
        for (Actor a : zombies) {
            a.act();
        }
    }

    @Override
    public void animate() {
        super.animate();
        for (Actor a : zombies) {
            a.animate();
        }
    }

    @Override
    public Image getImage() {
        Image img = super.getImage();

        Graphics g = img.getGraphics();

        for (Actor a : zombies) {
            Image sprite = a.getImage();
            int x = (a.getX() * getCellSize()) + ((getCellSize() - sprite.getWidth(null)) / 2);
            int y = (a.getY() * getCellSize()) + ((getCellSize() - sprite.getHeight(null)) / 2);
            g.drawImage(sprite, x, y, null);
        }

        g.dispose();

        return img;
    }

    @Override
    public void add(Actor a) {
        if (a instanceof Zombie.ZombieActor) {
            zombies.add(a);
        }
        else {
            super.add(a);
        }
    }

    public List<Zombie> getZombies() {
        return zombies.stream().map(z->((Zombie.ZombieActor)z).getZombie()).collect(Collectors.toList());
    }

    public String toString() {
        return name;
    }
}
