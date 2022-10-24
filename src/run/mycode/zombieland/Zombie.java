package run.mycode.zombieland;

import java.awt.*;

class Zombie {
    Actor zombie;

    private boolean isUndead;

    public Zombie(int xPos, int yPos, int dir, World world) {
        zombie = new ZombieActor(xPos, yPos, dir, world, this);
        isUndead = true;
    }

    public void die() {
        isUndead = false;
    }

    private boolean isUndead() {
        return isUndead;
    }

    static class ZombieActor extends Actor {

        private final Zombie zombie;

        private int dir;

        private static final Image[][] sprite = new Image[5][4];

        private static final String[] dirs = {"right", "down", "left", "up"};

        static {
            for (int d = 0; d < dirs.length; d++) {
                for (int i = 0; i < sprite[d].length; i++) {
                    sprite[d][i] = ImageLoader.loadResourceImage("/images/zombie-" + dirs[d] + "-" + i + ".png");
                }
            }

            sprite[dirs.length] = new Image[1];
            sprite[dirs.length][0] = ImageLoader.loadResourceImage(("/images/zombie-dead.png"));
        }

        public ZombieActor(int xPos, int yPos, int dir, World w, Zombie zombie) {
            super(xPos, yPos, w);
            this.zombie = zombie;
            this.dir = dir;
        }

        @Override
        public void act() {

        }

        @Override
        public Image getImage() {
            if (!zombie.isUndead()) {
                return sprite[sprite.length - 1][0];
            }
            else {
                return sprite[dir][getFrameNo(sprite[dir].length)];
            }
        }

        public Zombie getZombie() {
            return zombie;
        }

        public void turnRight() {
            dir = (dir + 1) % dirs.length;
        }

        public void turnLeft() {
            dir = dir - 1;
            while (dir < 0) {
                dir = dirs.length - dir;
            }
        }
    }
}
