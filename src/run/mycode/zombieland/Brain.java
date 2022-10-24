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
        Image img = numSprite.get(numBrains);
        if (img != null) {
            return img;
        }

        img = makeImage(numBrains);
        return img;
    }

    private static Image makeImage(int numBrains) {
        Image img = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics g = img.getGraphics();
        g.drawImage(sprite, 0, 0, null);

        if (numBrains > 1) {
            Font font = g.getFont();
            font = font.deriveFont(Font.BOLD, 10.0f);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();

            String count = "" + numBrains;
            Rectangle2D rect = fm.getStringBounds((count), g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, (int) rect.getWidth() + 2, (int) rect.getHeight() + 2);
            g.setColor(Color.WHITE);
            g.drawString(count, 1,  fm.getHeight() - fm.getDescent() - 1);
        }

        g.dispose();

        numSprite.put(numBrains, img);
        return img;
    }
}
