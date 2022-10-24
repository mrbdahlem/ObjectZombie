package run.mycode.zombieland;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class World {
    private final List<Actor> actors;

    private Image background;

    private int xSize;
    private int ySize;
    private int cellSize;

    public World() {
        actors = new ArrayList<>();
    }

    public World(int xSize, int ySize, int cellSize, Image background) {
        this();
        this.xSize = xSize;
        this.ySize = ySize;
        this.cellSize = cellSize;

        setBackground(background);
    }

    /**
     * Add an actor to the world
     * @param a the actor to add to the world
     */
    public void add(Actor a) {
        if (!actors.contains(a)) {
            actors.add(a);
        }
    }

    /**
     * Remove an actor from the world
     * @param a the actor to remove from the world
     */
    public void remove(Actor a) {
        actors.remove(a);
    }

    /**
     * Perform one action cycle, telling all Actors in the World to perform their own cycle.
     */
    public void act() {
        for (Actor a : actors) {
            a.act();
        }
    }

    /**
     * Animate the world and the actors it contains.
     */
    public void animate() {
        for (Actor a : actors) {
            a.animate();
        }
    }

    /**
     * Get the image of the current state of the world
     * @return an image showing the world and the actors it contains
     */
     public Image getImage() {
        final int width = xSize * cellSize;
        final int height = ySize * cellSize;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = img.getGraphics();

        g.drawImage(background, 0, 0, null);

        for (Actor a : actors) {
            Image sprite = a.getImage();
            int x = (a.getX() * cellSize) + ((cellSize - sprite.getWidth(null)) / 2);
            int y = (a.getY() * cellSize) + ((cellSize - sprite.getHeight(null)) / 2);
            g.drawImage(sprite, x, y, null);
        }

        g.dispose();

        return img;
    }

    /**
     * Set the background image for the world
     */
     void setBackground(Image img) {
        final int width = xSize * cellSize;
        final int height = ySize * cellSize;

        this.background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (img != null) {
            Graphics g = background.getGraphics();

            // Make sure the provided image has a valid width and height, then tile it over the background
            if (img.getWidth(null) > 0 && img.getHeight(null) > 0) {
                for (int x = 0; x < width; x += img.getWidth(null)) {
                    for (int y = 0; y < height; y += img.getHeight(null)) {
                        g.drawImage(img, x, y, null);
                    }
                }
            }

            g.dispose();
        }
    }

    /**
     * Retrieve the world's width in cells
     * @return the number of cells in the world in the x direction
     */
    public int getWidth() {
        return xSize;
    }

    /**
     * Retrieve the world's height in cells
     * @return the number of cells in the world in the y direction
     */
    public int getHeight() {
        return ySize;
    }

    /**
     * Retrieve the world's width in pixels
     * @return the number of pixels wide the world's image is
     */
    public int getFullWidth() {
        return xSize * cellSize;
    }

    /**
     * Retrieve the world's height in pixels
     * @return the number of pixels tall the world's image is
     */
    public int getFullHeight() {
        return ySize * cellSize;
    }

    /**
     * Retrieve references to all the Actors currently at position (x,y)
     * @param x the x coordinate of the location to check for Actors
     * @param y the y coordinate of the location to check for Actors
     * @return a List of all the actors at location (x,y)
     */
    public List<Actor> getActorsAt(int x, int y) {
        return actors.stream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .collect(Collectors.toList());
    }
}
