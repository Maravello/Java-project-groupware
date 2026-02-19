
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class ComboBoxItem {
    
    public ComboBoxItem(BufferedImage image, String title) {
        this.imageIcon = new ImageIcon(image, title);
        this.title = title;
    }
    
    public String toString() {
        return title;
    }
    
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    ImageIcon imageIcon;
    String title;
    
}