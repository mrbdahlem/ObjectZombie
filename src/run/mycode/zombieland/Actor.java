package run.mycode.zombieland;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * A base class for all objects appearing in a World in ZombieLand
 */
abstract class Actor {
    private int x;
    private int y;

    private int frame;
    private int frameOffset = 0;

    private final World world;

    private Image img;

    public Actor(int xPos, int yPos, World w) {
        this.x = xPos;
        this.y = yPos;
        this.world = w;
        w.add(this);
        this.frame = 0;
    }

    /**
     * Perform one action cycle.
     *     one frame of animation, move one square, etc
     */
    public abstract void act();

    /**
     * Get the actor's location x coordinate
     * @return the x coordinate of the actor's location
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x coordinate of the actor's location
     * @param x the x coordinate of the actor's new location
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the actor's location y coordinate
     * @return the y coordinate of the actor's location
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y coordinate of the actor's location
     * @param y the y coordinate of the actor's new location
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *Move this actor to a new location
     * @param newX the x coordinate of the actor's new location
     * @param newY the y coordinate of the actor's new location
     */
    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /**
     * Get a reference to the world that the actor is located in
     * @return the world the actor is located in.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the image for this actor
     * @return the image to display for this actor
     */
     Image getImage() {
        return img;
    }

    /**
     * Set the image for this actor
     * @param img the image to display for this actor
     */
     void setImage(Image img) {
        this.img = img;
     }

    /**
     * Show the next animation frame
     */
    void animate() {
        frame++;
    }

    /**
     * Get the current frame number from [0..mod)
     * @param mod the maximum frame number
     * @return the frame number in the range [0..mod)
     */
    int getFrameNo(int mod) {
        return (frame + frameOffset) % mod;
    }

    /**
     * Jump back to the first animation frame
     */
    void resetFrameNo() {
        frame = 0;
        frameOffset = 0;
    }

    /**
     * Set the frame offset for the first animation frame
     */
    void setFrameOffset(int offset) {
        frameOffset = offset;
    }

    static Image drawNumber(Image img, int num) {
        BufferedImage numSprite = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics g = numSprite.getGraphics();
        g.drawImage(img, 0, 0, null);

        Font font = g.getFont();
        font = font.deriveFont(Font.BOLD, 10.0f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        String count = "" + num;
        Rectangle2D rect = fm.getStringBounds((count), g);
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, (int) rect.getWidth() + 2, (int) rect.getHeight() + 2);
        g.setColor(Color.WHITE);
        g.drawString(count, 2,  1 + fm.getHeight() - fm.getDescent());

        g.dispose();

        return numSprite;
    }
}
