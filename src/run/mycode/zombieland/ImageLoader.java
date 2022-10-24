package run.mycode.zombieland;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

class ImageLoader {
    /**
     * Load the given resource file as an Image
     * @param filename the name of the resource to load
     * @return the loaded Image
     */
    public static Image loadResourceImage(String filename) {
        try {
            URL url = ZombieLand.class.getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("Resource " + filename + " cannot be found.");
            }
            return ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Could not load resource " + filename + ": " + e.getMessage());
            return null;
        }
    }
}
