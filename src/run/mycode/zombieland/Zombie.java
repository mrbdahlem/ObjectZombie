package run.mycode.zombieland;

import run.mycode.zombieland.exceptions.BucketStuckException;
import run.mycode.zombieland.exceptions.ZombieFallOffWorldException;
import run.mycode.zombieland.exceptions.ZombieHitWallException;

import java.awt.*;

class Zombie {
    private static final Image[][] sprite = new Image[5][4];

    static {
        Dir[] dirs = Dir.values();
        for (int d = 0; d < dirs.length; d++) {
            for (int i = 0; i < sprite[d].length; i++) {
                sprite[d][i] = ImageLoader.loadResourceImage("/images/zombie-" + dirs[d].toString() + "-" + i + ".png");
            }
        }

        sprite[dirs.length] = new Image[4];

        for (int i = 0; i < sprite[dirs.length].length; i++) {
            sprite[dirs.length][0] = ImageLoader.loadResourceImage(("/images/zombie-dead-" + i + ".png"));
        }
    }

    private final ZombieActor zombie;

    private boolean isUndead;
    private boolean hasWon;

    private int numBrains;

    @SuppressWarnings("unused")
    public Zombie(int xPos, int yPos, int dir, World world) {
        zombie = new ZombieActor(xPos, yPos, Dir.values()[dir], world, this);
        isUndead = true;
        hasWon = false;
    }

    @SuppressWarnings("unused")
    Zombie(int xPos, int yPos, int dir, int numBrains, World world) {
        this(xPos, yPos, dir, world);
        this.numBrains = numBrains;
    }

    public void die() {
        isUndead = false;
    }

    public boolean isUndead() {
        return isUndead;
    }

    public void win() { hasWon = true; }

    public boolean hasWon() { return hasWon; }

    public void turnRight() {
        zombie.dir = zombie.dir.toRight();
    }

    public void move() {
        final int newx = zombie.getX() + zombie.dir.dx();
        final int newy = zombie.getY() + zombie.dir.dy();

        final World w = zombie.getWorld();

        if (newx < 0 || newx >= w.getWidth() ||
                newy < 0 || newy >= w.getHeight()) {
            throw new ZombieFallOffWorldException();
        }

        for (Actor a : w.getActorsAt(newx, newy)) {
            if (a instanceof Wall) {
                throw new ZombieHitWallException();
            }

            if (a instanceof Bucket) {
                if (!((Bucket)a).push(zombie.dir)) {
                    throw new BucketStuckException();
                }
            }
        }

        zombie.setPosition(newx, newy);
    }

    class ZombieActor extends Actor {

        private final Zombie zombie;

        private Dir dir;

        public ZombieActor(int xPos, int yPos, Dir dir, World w, Zombie zombie) {
            super(xPos, yPos, w);
            this.zombie = zombie;
            this.dir = dir;
        }

        @Override
        public void act() {

        }

        public Zombie getZombie() {
            return this.zombie;
        }

        @Override
        public Image getImage() {
            Image img;

            int dirnum = dir.ordinal();
            if (zombie.hasWon()) {
                img = sprite[1][1];
            }
            else if (!zombie.isUndead()){ /* !undead == dead */
                img = sprite[sprite.length - 1][dirnum];
            }
            else {
                img = sprite[dirnum][getFrameNo(sprite[dirnum].length)];
            }

            if (numBrains > 1) {
                img = drawNumber(img, numBrains);
            }

            return img;
        }

    }
}
