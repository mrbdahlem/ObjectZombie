package run.mycode.zombieland;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Brain extends Actor {
    private int numBrains;

    private static final Image sprite = ImageLoader.loadResourceImage("/images/brain.png");
    private static final Map<Integer, Image> numSprite = new HashMap<>();

    public Brain(int xPos, int yPos, int count, World world) {
        super(xPos, yPos, world);
        this.numBrains = count;
    }
    /**
     * Disembodied Brains don't do much.
     */
    @Override
    public void act() { }

    @Override
    public Image getImage() {
        if (numBrains == 1) {
            return sprite;
        }

        Image img = numSprite.get(numBrains);
        if (img != null) {
            return img;
        }

        img = makeImage(numBrains);
        return img;
    }

    private static Image makeImage(int numBrains) {
        Image img = drawNumber(sprite, numBrains);
        numSprite.put(numBrains, img);
        return img;
    }

    static Image drawNumber(Image img, int num) {
        final int width = img.getWidth(null);
        final int height = img.getHeight(null);
        BufferedImage numSprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = numSprite.getGraphics();
        g.drawImage(img, 0, 0, null);

        Font font = g.getFont();
        font = font.deriveFont(Font.BOLD, 20.0f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        String count = "" + num;
        Rectangle2D rect = fm.getStringBounds((count), g);
        g.setColor(Color.BLACK);

        final int x = (int)((width - rect.getWidth()) / 2);
        final int y = (int)((height - rect.getHeight()) / 2);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                g.drawString(count, x + dx,  y + dy + fm.getHeight() - fm.getDescent());
            }
        }

        g.setColor(Color.WHITE);
        g.drawString(count, x, y + fm.getHeight() - fm.getDescent());

        g.dispose();

        return numSprite;
    }
}
