package run.mycode.zombieland.viewer;

import run.mycode.zombieland.ZombieLand;

import javax.swing.*;
import java.awt.*;

public class WorldView extends JPanel {
    ZombieLand world;

    public WorldView() {
        super();
    }

    public void setWorld(ZombieLand zombieLand) {
        this.world = zombieLand;
        resize();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintComponents(g);

        int x = (getWidth() - world.getFullWidth()) / 2;
        int y = (getHeight() - world.getFullHeight()) / 2;

        if (this.world != null) {
            g.drawImage(world.getImage(), x, y, null);
        }
        else {
            g.clearRect(0,0, getWidth(), getHeight());
        }
    }

    public void resize() {
        SwingUtilities.invokeLater(()->{
            Dimension size = new Dimension(world.getFullWidth(), world.getFullHeight());
            Container view =  this.getParent();

            size.setSize(Math.max(view.getWidth(), size.getWidth()), Math.max(view.getHeight(), size.getHeight()));
            this.setSize(size);
            this.setPreferredSize(size);

            view.getParent().setPreferredSize(size);
            repaint();
        });
    }
}
