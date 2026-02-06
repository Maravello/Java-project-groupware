import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

// Fenêtre principale modifiée pour accepter les flux
class FenBoutonsDyn extends JFrame implements ActionListener {
    final int NBOUTONS = 3;
    private JButton tabBout[];
    private JCheckBox coche;
    private PrintWriter out;
    private BufferedReader in;
    private FenText fenChat; // Instance unique pour le chat

    public FenBoutonsDyn(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
        setTitle("Hierarchie de menus");
        setSize(350, 100);
        Container contenu = getContentPane();
        contenu.setLayout(new FlowLayout());

        tabBout = new JButton[NBOUTONS];
        tabBout[0] = new JButton("DESSIN");
        tabBout[1] = new JButton("DESSINER");
        tabBout[2] = new JButton("ECRIRE");

        for (int i = 0; i < NBOUTONS; i++) {
            contenu.add(tabBout[i]);
            tabBout[i].addActionListener(this);
        }

        coche = new JCheckBox("case");
        contenu.add(coche);
        coche.addActionListener(this);
    }

    // Méthode pour transmettre les messages reçus à la fenêtre de texte
    public void diffuserMessage(String msg) {
        if (fenChat != null) {
            fenChat.ajouterMessage(msg);
        }
    }

    public void actionPerformed(ActionEvent ev) {
        Object source0 = ev.getSource();
        if (source0 == coche) {
            if (coche.isSelected()) {
                for (int i = 0; i < NBOUTONS; i++) tabBout[i].setEnabled(true);
            }
        } else {
            JButton source = (JButton) ev.getSource();
            if (source == tabBout[0]) {
                new Fenetre().setVisible(true);
            }
            if (source == tabBout[1]) {
                new FenMenu().setVisible(true);
            }
            if (source == tabBout[2]) {
                // On crée la fenêtre de chat avec le flux de sortie
                if (fenChat == null) fenChat = new FenText(out);
                fenChat.setVisible(true);
            }
            source.setEnabled(false);
        }
    }
}

// Fenêtre de Chat modifiée
class FenText extends JFrame implements ActionListener {
    private JTextField saisie;
    private JTextArea historique;
    private PrintWriter out;

    public FenText(PrintWriter out) {
        this.out = out;
        setTitle("Chat Réseau");
        setBounds(10, 600, 400, 300);
        Container contenu = getContentPane();
        contenu.setLayout(new BorderLayout());

        historique = new JTextArea();
        historique.setEditable(false);
        contenu.add(new JScrollPane(historique), BorderLayout.CENTER);

        saisie = new JTextField();
        saisie.addActionListener(this);
        contenu.add(saisie, BorderLayout.SOUTH);
    }

    public void ajouterMessage(String msg) {
        historique.append("L'autre : " + msg + "\n");
    }

    public void actionPerformed(ActionEvent e) {
        String texte = saisie.getText();
        if (!texte.isEmpty()) {
            out.println(texte); // Envoi au réseau
            historique.append("Moi : " + texte + "\n");
            saisie.setText("");
        }
    }
}