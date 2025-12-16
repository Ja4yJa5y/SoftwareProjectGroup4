package library;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private final Image background;

    public BackgroundPanel(String imagePath) {
        // If your ImageUtils loads from resources, use it. Otherwise Toolkit works for file paths.
        // background = new ImageIcon(getClass().getResource("/" + imagePath)).getImage();
        background = new ImageIcon(imagePath).getImage();
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
