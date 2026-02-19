
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.color.*;

public class NetDrawImage extends JComponent implements MouseListener, MouseMotionListener {
    
    public NetDrawImage(BufferedWriter bwriter, int width, int height, JComboBox lineStyle, JComboBox lineColor, JComboBox lineThickness, JCheckBox antiAliasCheckBox, JCheckBox filledCheckBox) {
        this.bwriter = bwriter;
        this.lineStyle = lineStyle;
        this.lineColor = lineColor;
        this.lineThickness = lineThickness;
        this.antiAliasCheckBox = antiAliasCheckBox;
        this.filledCheckBox = filledCheckBox;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        imageGraphics = (Graphics2D)image.getGraphics();
        imageGraphics.setColor(Color.white);
        imageGraphics.fillRect(0, 0, width-1, height-1);
        
        this.updateUI();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

    }
    
    public void mouseDragged(MouseEvent e) {
        if (enabled) {
            e.consume();
            int newX = e.getX();
            int newY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("A main levé")) {
                drawBufferedLine(lastX, lastY, newX, newY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + newX + " " + newY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                lastX = newX;
                lastY = newY;
            }
            else if (style.equals("Line")) {
                paintComponent(this.getGraphics());
                this.drawClientLine(lastX, lastY, newX, newY);
            }
            else if (style.equals("Rectangle")) {
                paintComponent(this.getGraphics());
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, filledCheckBox.isSelected());
            }
            else if (style.equals("Oval")) {
                paintComponent(this.getGraphics());
                this.drawClientOval((lastX+newX)/2 - (newX-lastX), (lastY+newY)/2 - (newY-lastY), newX-lastX, newY-lastY, filledCheckBox.isSelected());
            }
            else if (style.equals("Text")) {
                Graphics g = this.getGraphics();
                paintComponent(g);
                g.drawString("Text...", newX, newY);
            }
            else if (style.equals("Pseudo-UML")) {
                paintComponent(this.getGraphics());
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, false);
                if (newY > lastY + 20) {
                    this.drawClientLine(lastX, lastY+20, newX, lastY+20);
                }
            }
            else if (style.equals("Effacer zone")) {
                paintComponent(this.getGraphics());
                currentColor = Color.yellow;
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, false);
            }
        }
    }
    
    
    public void drawBufferedLine(int x1, int y1, int x2, int y2, Color color, Stroke stroke) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        componentGraphics.drawLine(x1, y1, x2, y2);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        imageGraphics.drawLine(x1, y1, x2, y2);
    }
    
    public void drawBufferedBox(int x1, int y1, int width, int height, Color color, Stroke stroke, boolean filled) {
        // No need to anti-alias this really...
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        
        if (filled) {
            componentGraphics.fillRect(x1, y1, width, height);
            imageGraphics.fillRect(x1, y1, width, height);
        }
        else {
            componentGraphics.drawRect(x1, y1, width, height);
            imageGraphics.drawRect(x1, y1, width, height);
        }
    }
    
    public void drawBufferedOval(int x1, int y1, int width, int height, Color color, Stroke stroke, boolean filled) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        
        if (filled) {
            componentGraphics.fillOval(x1, y1, width, height);
            imageGraphics.fillOval(x1, y1, width, height);
        }
        else {
            componentGraphics.drawOval(x1, y1, width, height);
            imageGraphics.drawOval(x1, y1, width, height);
        }
    }
    
    public void drawBufferedText(int x, int y, Color color, String text) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setColor(color);
        componentGraphics.drawString(text, x, y);
        imageGraphics.setColor(color);
        imageGraphics.drawString(text, x, y);
    }
    
    public void drawClientLine(int x1, int y1, int x2, int y2) {
        // Note that this is not antialiased
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        componentGraphics.drawLine(x1, y1, x2, y2);
    }
    
    public void drawClientBox(int x1, int y1, int width, int height, boolean filled) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        if (filled) {
            componentGraphics.fillRect(x1, y1, width, height);
        }
        else {
            componentGraphics.drawRect(x1, y1, width, height);
        }
    }
    
    public void drawClientOval(int x1, int y1, int width, int height, boolean filled) {

        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        if (filled) {
            componentGraphics.fillOval(x1, y1, width, height);
        }
        else {
            componentGraphics.drawOval(x1, y1, width, height);
        }
    }
    
    public void clearGraphics() {
        imageGraphics.setColor(Color.white);
        imageGraphics.fillRect(0, 0, image.getWidth()-1, image.getWidth()-1);
        repaint();
    }
    
    public void setBufferedWriter(BufferedWriter bwriter) {
        this.bwriter = bwriter;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void mousePressed(MouseEvent e) {
        if (enabled) {
            currentColor = Color.decode((String)lineColor.getSelectedItem().toString());
            currentStroke = new BasicStroke(Float.parseFloat((String)lineThickness.getSelectedItem().toString()));
            e.consume();
            lastX = e.getX();
            lastY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("A main levé")) {
                this.drawBufferedLine(lastX, lastY, lastX, lastY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + lastX + " " + lastY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Line")) {
               
            }
            else if (style.equals("Rectangle")) {
        
            }
            else if (style.equals("Oval")) {
         
            }
            else if (style.equals("Text")) {
                Graphics g = this.getGraphics();
                g.drawString("Text...", lastX, lastY);
            }
            else if (style.equals("Pseudo-UML")) {
                
            }
            else if (style.equals("Effacer zone")) {
                
            }
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if (enabled) {
            e.consume();
            int newX = e.getX();
            int newY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("A main levé")) {
              
            }
            else if (style.equals("Line")) {
                this.drawBufferedLine(lastX, lastY, newX, newY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + newX + " " + newY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Rectangle")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                try {
                    sendLine("rectangle " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth() + " " + filledCheckBox.isSelected());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Oval")) {
                this.drawBufferedOval((lastX+newX)/2 - (newX-lastX), (lastY+newY)/2 - (newY-lastY), newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                try {
                    sendLine("oval " + ((lastX+newX)/2 - (newX-lastX)) + " " + ((lastY+newY)/2 - (newY-lastY)) + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth() + " " + filledCheckBox.isSelected());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Text")) {
                Graphics2D g = (Graphics2D)this.getGraphics();
                String text = null;
                try {
                    text = JOptionPane.showInputDialog(null, "Please enter your text to add to the drawing view:", "Drawing Text", JOptionPane.QUESTION_MESSAGE).trim();
                }
                catch (Exception any) {
                    paintComponent(g);
                    return;
                }
                if (text == null || text == "") {
                    paintComponent(g);
                    return;
                }
                paintComponent(g);
                this.drawBufferedText(newX, newY, currentColor, text);
                try {
                    sendLine("text " + newX + " " + newY + " " + currentColor.getRGB() + " " + text);
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                antiAliasCheckBox.requestFocus();
            }
            else if (style.equals("Pseudo-UML")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                if (newY > lastY + 20) {
                    this.drawBufferedLine(lastX, lastY+20, newX, lastY+20, currentColor, currentStroke);
                }
                try {
                    sendLine("uml " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Effacer zone")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, Color.white, currentStroke, true);
                try {
                    sendLine("box " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + Color.white.getRGB() + " " + currentStroke.getLineWidth()+ " " + "true");
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                repaint();
            }
        }
    }
    public void mouseMoved(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image, null, null);
    }
    
    private synchronized void sendLine(String line) throws IOException {
        bwriter.write(line);
        bwriter.newLine();
        bwriter.flush();
    }
    
    private BufferedImage image;
    private BufferedWriter bwriter = null;
    private JComboBox lineStyle = null;
    private JComboBox lineColor = null;
    private JComboBox lineThickness = null;
    private JCheckBox antiAliasCheckBox = null;
    private JCheckBox filledCheckBox = null;
    private boolean mouseDown = false;
    private int lastX = 0;
    private int lastY = 0;
    
    private Color currentColor = null;
    private BasicStroke currentStroke = null;
    
    private Graphics2D imageGraphics = null;
    
    private boolean enabled = false;
}