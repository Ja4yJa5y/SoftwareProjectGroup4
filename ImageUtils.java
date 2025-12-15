package library;

import javax.swing.*;
import java.awt.*;

public class ImageUtils {

    public static ImageIcon loadIcon(String path, int width, int height) {
        java.net.URL url = ImageUtils.class.getResource(  path);
        if (url == null) {
            System.err.println("Image not found: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // For background labels if needed
    public static JLabel createBackgroundLabel(String path, int width, int height) {
        ImageIcon icon = loadIcon(path, width, height);
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
}
