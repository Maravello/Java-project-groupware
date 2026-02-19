
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class ImageStore {
    
    private ImageStore() {
        
    }

    public static ComboBoxItem getColorItem(String hexColor) {
        BufferedImage image = new BufferedImage(18, 18, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setColor(Color.decode(hexColor));
        g.fillRect(0, 0, 17, 17);
        return new ComboBoxItem(image, hexColor);
    }
    
    public static ComboBoxItem getLineThicknessItem(String thickness) {
        BufferedImage image = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, 17, 17);
        g.setColor(new Color(51, 51, 153, 255));
        g.setStroke(new BasicStroke(Float.parseFloat(thickness)));
        g.drawLine(1, 9, 16, 9);
        return new ComboBoxItem(image, thickness);
    }

    public static BufferedImage getImage(int imageNum) {
        BufferedImage image = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, 17, 17);
        
        g.setColor(new Color(51, 51, 153, 255));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        switch (imageNum) {
            case 0: // A main levé
                g.drawLine(1, 1, 4, 2);
                g.drawLine(4, 2, 5, 5);
                g.drawLine(5, 5, 7, 10);
                g.drawLine(7, 10, 13, 15);
                g.drawLine(13, 15, 16, 16);
                break;
            case 1: // Line
                g.drawLine(2, 2, 15, 15);
                break;
            case 2: // Rectangle
                g.drawRect(2, 2, 15, 15);
                break;
            case 3: // Oval
                g.drawOval(2, 2, 15, 15);
                break;
            case 4: // Text
                g.drawString("Bla", 0, 13);
                break;
            case 5: // Pseudo-UML
                g.drawRect(3, 1, 11, 15);
                g.drawLine(3, 4, 14, 4);
                break;
            case 6: // Effacer zone
                g.drawLine(2, 2, 15, 15);
                g.drawLine(2, 15, 15, 2);
                g.drawRect(2, 2, 14, 14);
                break;
        }
        
        return image;
    }
    
}