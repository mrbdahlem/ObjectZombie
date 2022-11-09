package run.mycode.zombieland;

import run.mycode.zombieland.exceptions.ZombieFallOffWorldException;

import java.awt.*;

class Zombie {
    private static final Image[][] sprite = new Image[5][4];

    private static final String[] dirs = {"right", "down", "left", "up"};

    static {
        for (int d = 0; d < dirs.length; d++) {
            for (int i = 0; i < sprite[d].length; i++) {
                sprite[d][i] = ImageLoader.loadResourceImage("/images/zombie-" + dirs[d] + "-" + i + ".png");
            }
        }

        sprite[dirs.length] = new Image[4];

        for (int i = 0; i < sprite[dirs.length].length; i++) {
            sprite[dirs.length][0] = ImageLoader.loadResourceImage(("/images/zombie-dead-" + i + ".png"));
        }
    }

    private final Actor zombie;

    private boolean isUndead;
    private boolean hasWon;

    private int numBrains;

    @SuppressWarnings("unused")
    public Zombie(int xPos, int yPos, int dir, World world) {
        zombie = new ZombieActor(xPos, yPos, dir, world, this);
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

    class ZombieActor extends Actor {

        private final Zombie zombie;

        private int dir;

        public ZombieActor(int xPos, int yPos, int dir, World w, Zombie zombie) {
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

        public void turnRight() {
            dir = (dir + 1) % dirs.length;
        }

        public void move() {
            //dirs = {"right", "down", "left", "up"}
            int[] dx = {1, 0, -1, 0};
            int[] dy = {0, 1, 0, -1};

            int newx = getX() + dx[dir];
            int newy = getY() + dy[dir];

            World w = getWorld();

            if (newx < 0 || newx >= w.getWidth() ||
                    newy < 0 || newy >= w.getHeight()) {
                throw new ZombieFallOffWorldException();
            }

        }

        @Override
        public Image getImage() {
            Image img;

            if (zombie.hasWon()) {
                img = sprite[1][1];
            }
            else if (!zombie.isUndead()){ /* !undead == dead */
                img = sprite[sprite.length - 1][dir];
            }
            else {
                img = sprite[dir][getFrameNo(sprite[dir].length)];
            }

            if (numBrains > 1) {
                img = drawNumber(img, numBrains);
            }

            return img;
        }

    }
}
