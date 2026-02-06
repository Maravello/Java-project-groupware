import java.awt.*;
import javax.swing.*;

// Fenêtre pour le dessin
class Fenetre extends JFrame {
    public Fenetre() {
        setTitle("Fenetre de Dessin");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contenu = getContentPane();
        contenu.setLayout(new BorderLayout());
        contenu.add(new JLabel("Zone de dessin"), BorderLayout.CENTER);
    }
}
