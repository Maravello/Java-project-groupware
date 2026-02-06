import java.awt.*;
import javax.swing.*;

// Fenêtre de menu
class FenMenu extends JFrame {
    public FenMenu() {
        setTitle("Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contenu = getContentPane();
        contenu.setLayout(new BorderLayout());
        contenu.add(new JLabel("Fenêtre de Menu"), BorderLayout.CENTER);
    }
}
