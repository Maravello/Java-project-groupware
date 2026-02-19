
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.net.*;
import java.io.*;

public class NetDrawClient extends JComponent {
    
    public NetDrawClient(String title, int width, int height, int graphicsWidth, int graphicsHeight) {
        
        frame = new JFrame(title);

        // menu       
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menu1 = new JMenu("Serveur");
        menu1.setMnemonic(KeyEvent.VK_F);
        menu1.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu1);

        JMenuItem menuItem1 = new JMenuItem("Connecter", KeyEvent.VK_C);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem1.getAccessibleContext().setAccessibleDescription("Connecter au Serveur");
        menu1.add(menuItem1);

        JMenuItem menuItem2 = new JMenuItem("Disconnecter", KeyEvent.VK_D);
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuItem2.getAccessibleContext().setAccessibleDescription("Deconnecter du server");
        menu1.add(menuItem2);

        JMenuItem menuItem3 = new JMenuItem("Quitter", KeyEvent.VK_X);
        menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        menuItem3.getAccessibleContext().setAccessibleDescription("Quitter le client");
        menu1.add(menuItem3);
        
        JMenu menu2 = new JMenu("Dessin");
        menu2.setMnemonic(KeyEvent.VK_R);
        menu2.getAccessibleContext().setAccessibleDescription("Drawing options");
        menuBar.add(menu2);
        
        JMenuItem menuItem1_1 = new JMenuItem("effacer", KeyEvent.VK_L);
        menuItem1_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem1_1.getAccessibleContext().setAccessibleDescription("Efface la fenetre de dessin de tout les utilisateurs");
        menu2.add(menuItem1_1);
        
        // Fin de reglage menu.
        

        // Set up drop down boxes
        lineStyle.setRenderer(new ComboBoxCellRenderer(lineStyle));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(0), "A main levé"));
       //lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(1), "Line"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(2), "Rectangle"));
        //lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(3), "Oval"));
        //lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(4), "Text"));
        //lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(5), "Pseudo-UML"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(6), "Effacer zone"));
        
        lineColor.setRenderer(new ComboBoxCellRenderer(lineColor));
        lineColor.addItem(ImageStore.getColorItem("#000000"));
        /*lineColor.addItem(ImageStore.getColorItem("#999999"));
        lineColor.addItem(ImageStore.getColorItem("#ffffff"));*/
        // cont
        /*lineColor.addItem(ImageStore.getColorItem("#990000"));
        lineColor.addItem(ImageStore.getColorItem("#009900"));
        lineColor.addItem(ImageStore.getColorItem("#000099"));
        lineColor.addItem(ImageStore.getColorItem("#999900"));
        lineColor.addItem(ImageStore.getColorItem("#990099"));
        lineColor.addItem(ImageStore.getColorItem("#009999"));*/
        // cont
	    lineColor.addItem(ImageStore.getColorItem("#ff0000"));
        lineColor.addItem(ImageStore.getColorItem("#00ff00"));
        lineColor.addItem(ImageStore.getColorItem("#0000ff"));
        /*lineColor.addItem(ImageStore.getColorItem("#ffff00"));
        lineColor.addItem(ImageStore.getColorItem("#ff00ff"));
        lineColor.addItem(ImageStore.getColorItem("#00ffff"));*/

        lineThickness.setRenderer(new ComboBoxCellRenderer(lineThickness));
        //lineThickness.addItem(ImageStore.getLineThicknessItem("1"));
        lineThickness.addItem(ImageStore.getLineThicknessItem("2"));
        //lineThickness.addItem(ImageStore.getLineThicknessItem("4"));
        //lineThickness.addItem(ImageStore.getLineThicknessItem("8"));

        // Set default JComboBox values.
        lineStyle.setSelectedIndex(0);
        lineColor.setSelectedIndex(0);
        lineThickness.setSelectedIndex(0);

        //msgTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        msgScrollPane.getViewport().setView(msgTextArea);
        msgTextArea.setEditable(false);
        msgTextArea.setLineWrap(true);

        image = new NetDrawImage(bwriter, graphicsWidth, graphicsHeight, lineStyle, lineColor, lineThickness, antiAliasCheckBox, filledCheckBox);
        graphicsScrollPane.getViewport().setView(image);
        graphicsScrollPane.setPreferredSize(new Dimension(640, 240));
        image.setEnabled(false);

        try {
            while (userName == null || userName.trim().equals("")) {
                userName = JOptionPane.showInputDialog(null, "Pseudo:", "Entrer votre pseudo", JOptionPane.QUESTION_MESSAGE).trim();
            }
        }
        catch (Exception e) {
            System.exit(0);
        }
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel(userName));
        controlPanel.add(inputText);
        inputText.setEnabled(false);

        JPanel graphicsToolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graphicsToolBarPanel.add(lineStyle);
        graphicsToolBarPanel.add(lineColor);
        //graphicsToolBarPanel.add(lineThickness);
       //graphicsToolBarPanel.add(antiAliasCheckBox);
       //graphicsToolBarPanel.add(filledCheckBox);
        JToolBar graphicsToolBar = new JToolBar();
        graphicsToolBar.add(graphicsToolBarPanel);

        JPanel graphicsOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graphicsOptions.add(graphicsToolBar);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphicsScrollPane, msgScrollPane);
        splitPane.setOneTouchExpandable(true);

        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(graphicsOptions, BorderLayout.NORTH);
        pane.add(splitPane, BorderLayout.CENTER);
        pane.add(controlPanel, BorderLayout.SOUTH);
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (socket != null) {
                    try {
                        bwriter.write("quit");
                        bwriter.newLine();
                        bwriter.flush();
                        socket.close();
                    }
                    catch (IOException ie) {
                        // Do nothing.
                    }
                    socket = null;
                }
                System.exit(0);
            }
        });

        // Input Text
        inputText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputText.getText();
                try {
                    bwriter.write("msg " + userName + " " + message);
                    bwriter.newLine();
                    bwriter.flush();
                    inputText.setText("");
                }
                catch (IOException ie) {
                    image.setEnabled(false);
                    inputText.setEnabled(false);
                    inputText.setText("*** Deconnecter ***");
                }
            }
        });
        
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String serverHostname = JOptionPane.showInputDialog("Adresse du serveur:");
                if (serverHostname == null) {
                    return;
                }
                try {
                    socket = new Socket(serverHostname, 1515);
                    breader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                    bwriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
                    bwriter.write("join " + userName);
                    bwriter.newLine();
                    bwriter.flush();
                    image.setBufferedWriter(bwriter);
                    InputThread inputThread = new InputThread(breader, msgTextArea, msgScrollPane, inputText, image);
                    inputThread.start();
                    inputText.setText("");
                    inputText.setEnabled(true);
                    inputText.requestFocus();
                    image.setEnabled(true);
                    image.clearGraphics();
                }
                catch (IOException ie) {
                    JOptionPane.showMessageDialog(null, "Impossible de se connecter à " + serverHostname + " sur le port 1515", "Probleme de connection", JOptionPane.ERROR_MESSAGE); 
                }
            }
        });

        // Disconnect
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket != null) {
                    try {
                        bwriter.write("quit");
                        bwriter.newLine();
                        bwriter.flush();
                        socket.close();
                    }
                    catch (IOException ie) {
                    }
                    socket = null;
                }
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket != null) {
                    try {
                        bwriter.write("quit");
                        bwriter.newLine();
                        bwriter.flush();
                        socket.close();
                    }
                    catch (IOException ie) {
                    }
                    socket = null;
                }
                System.exit(0);
            }
        });

        menuItem1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bwriter.write("effacer " + userName);
                    bwriter.newLine();
                    bwriter.flush();
                }
                catch (Exception ie) {
                    
                }
                if (image != null ) {
                    image.clearGraphics();
                }
            }
        });
        
        frame.setSize(width, height);
        //frame.setResizable(false);
        
        SwingUtilities.updateComponentTreeUI(this);
        frame.setVisible(true);
        
    }
    
    private JFrame frame;
    private JTextArea msgTextArea = new JTextArea();
    private JTextField inputText = new JTextField(" Veuillez vous connecter ", 40);
    private JScrollPane graphicsScrollPane = new JScrollPane();
    private JScrollPane msgScrollPane = new JScrollPane();
    private NetDrawImage netDrawImage = null;
    private NetDrawImage image;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JComboBox lineStyle = new JComboBox(); // (new Object[] {"Freehand", "Line", "Rectangle", "Oval", "Text", "Pseudo-UML", "Effacer zone"});
    private JComboBox lineColor = new JComboBox(); // (new Object[] {"#000000", "#00ff00", "#0000ff"});
    private JComboBox lineThickness = new JComboBox(); //(new Object[] {"1.0", "2.0", "4.0"});
    private JCheckBox antiAliasCheckBox = new JCheckBox("Anti-alias", true);
    private JCheckBox filledCheckBox = new JCheckBox("filled", false);

    private Socket socket = null;
    private BufferedReader breader = null;
    private BufferedWriter bwriter = null;

    private String userName = null;

}