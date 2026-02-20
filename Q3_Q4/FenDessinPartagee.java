import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

/**
 * Fenêtre de dessin partagée entre plusieurs utilisateurs.
 */
public class FenDessinPartagee extends JFrame {
    private DrawingPanel drawingPanel;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JLabel statusLabel;
    
    public FenDessinPartagee(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
        
        setTitle("Dessin Partagé");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Container contenu = getContentPane();
        contenu.setLayout(new BorderLayout());
        
        // Panneau de dessin
        drawingPanel = new DrawingPanel(out);
        contenu.add(drawingPanel, BorderLayout.CENTER);
        
        // Panneau de contrôle
        JPanel controlPanel = new JPanel();
        JButton clearBtn = new JButton("Effacer");
        clearBtn.addActionListener(e -> drawingPanel.clear());
        controlPanel.add(clearBtn);
        
        statusLabel = new JLabel("Dessin partagé actif");
        controlPanel.add(statusLabel);
        
        contenu.add(controlPanel, BorderLayout.SOUTH);
        
        // Thread de réception des commandes de dessin des autres clients
        new Thread(() -> {
            try {
                Object obj;
                boolean firstCommand = true;
                while ((obj = in.readObject()) != null) {
                    if (obj instanceof DrawingCommand) {
                        DrawingCommand cmd = (DrawingCommand) obj;
                        
                        // La première commande reçue définit la couleur du client
                        if (firstCommand) {
                            drawingPanel.setClientColor(cmd.getClientColor());
                            firstCommand = false;
                        }
                        
                        drawingPanel.executeCommand(cmd);
                        statusLabel.setText("Mise à jour reçue de " + cmd.getClientId());
                    }
                }
            } catch (EOFException e) {
                System.out.println("Connexion fermée");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

/**
 * Panneau de dessin interactif.
 */
class DrawingPanel extends JPanel {
    private BufferedImage image;
    private Graphics2D g2d;
    private ObjectOutputStream out;
    private int currentColor = Color.BLACK.getRGB();
    private int clientColor = Color.BLACK.getRGB();  // Couleur attribuée au client
    private int startX, startY;
    
    public DrawingPanel(ObjectOutputStream out) {
        this.out = out;
        setBackground(Color.WHITE);
        
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 800, 600);
        g2d.setColor(Color.BLACK);
        
        // Écoute pour recevoir la couleur du client depuis le serveur
        new Thread(() -> {
            try {
                // On attendra la première commande qui contiendra la couleur du client
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                drawLine(startX, startY, e.getX(), e.getY());
            }
        };
        
        addMouseListener(mouseListener);
    }
    
    /**
     * Définit la couleur du client.
     */
    public void setClientColor(int color) {
        this.clientColor = color;
        this.currentColor = color;
    }
    
    /**
     * Dessine une ligne et l'envoie aux autres clients.
     */
    private void drawLine(int x1, int y1, int x2, int y2) {
        g2d.setColor(new Color(clientColor));
        g2d.drawLine(x1, y1, x2, y2);
        repaint();
        
        try {
            DrawingCommand cmd = new DrawingCommand(
                DrawingCommand.Type.LINE, x1, y1, x2, y2, clientColor, "SELF", clientColor
            );
            out.writeObject(cmd);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Exécute une commande de dessin reçue.
     */
    public void executeCommand(DrawingCommand cmd) {
        // Utilise la couleur du client qui a émis la commande
        int drawColor = cmd.getClientColor();
        
        switch (cmd.getType()) {
            case LINE:
                g2d.setColor(new Color(drawColor));
                g2d.drawLine(cmd.getX1(), cmd.getY1(), cmd.getX2(), cmd.getY2());
                break;
            case RECT:
                g2d.setColor(new Color(drawColor));
                g2d.drawRect(cmd.getX1(), cmd.getY1(), 
                           cmd.getX2() - cmd.getX1(), cmd.getY2() - cmd.getY1());
                break;
            case CIRCLE:
                g2d.setColor(new Color(drawColor));
                int diameter = (int) Math.sqrt(Math.pow(cmd.getX2() - cmd.getX1(), 2) + 
                                              Math.pow(cmd.getY2() - cmd.getY1(), 2));
                g2d.drawOval(cmd.getX1(), cmd.getY1(), diameter, diameter);
                break;
            case CLEAR:
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, 800, 600);
                break;
        }
        repaint();
    }
    
    /**
     * Efface le dessin.
     */
    public void clear() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 800, 600);
        repaint();
        
        try {
            DrawingCommand cmd = new DrawingCommand(DrawingCommand.Type.CLEAR, "SELF");
            out.writeObject(cmd);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
