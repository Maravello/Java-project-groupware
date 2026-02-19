
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.BasicStroke;

public class InputThread extends Thread {

    public InputThread(BufferedReader breader, JTextArea textArea, JScrollPane scrollPane, JTextField inputText, NetDrawImage image) {
        this.breader = breader;
        this.textArea = textArea;
        this.scrollPane = scrollPane;
        this.inputText = inputText;
        this.image = image;
    }

    public void run() {
        boolean running = true;
        try {
            while (running) {
                String input = breader.readLine();
                if (input == null) {
                    break;
                }
                StringTokenizer tokenizer = new StringTokenizer(input);
                if (tokenizer.countTokens() < 2) {
                    continue;
                }
                String type = tokenizer.nextToken();
                if (type.equals("msg")) {
                    String newText = "<" + tokenizer.nextToken() + ">";
                    while (tokenizer.hasMoreTokens()) {
                        newText = newText + " " + tokenizer.nextToken();
                    }
                    textArea.append(newText + "\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
                else if (type.equals("alerte")) {
                    String newText = "*";
                    while (tokenizer.hasMoreTokens()) {
                        newText = newText + " " + tokenizer.nextToken();
                    }
                    textArea.append(newText + "\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
                else if (type.equals("effacer")) {
                    String by = tokenizer.nextToken();
                    textArea.append("* Dessin effacé par " + by + " .\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                    image.clearGraphics();
                }
                else if (type.equals("line")) {
                    try {
                        int x1 = Integer.parseInt(tokenizer.nextToken());
                        int y1 = Integer.parseInt(tokenizer.nextToken());
                        int x2 = Integer.parseInt(tokenizer.nextToken());
                        int y2 = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        image.drawBufferedLine(x1, y1, x2, y2, color, stroke);
                    }
                    catch (Exception e) {
                        System.out.println("Possible de perte de ligne: " + input);
                    }
                }
                else if (type.equals("rectangle")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int width = Integer.parseInt(tokenizer.nextToken());
                        int height = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        boolean filled = Boolean.valueOf(tokenizer.nextToken()).booleanValue();
                        image.drawBufferedBox(x, y, width, height, color, stroke, filled);
                    }
                    catch (Exception e) {
                        System.out.println("Possible de perte de rectangle: " + input);
                    }
                    image.repaint();
                }
                else if (type.equals("oval")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int width = Integer.parseInt(tokenizer.nextToken());
                        int height = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        boolean filled = Boolean.valueOf(tokenizer.nextToken()).booleanValue();
                        image.drawBufferedOval(x, y, width, height, color, stroke, filled);
                    }
                    catch (Exception e) {
                        System.out.println("Possible de perte de ovale: " + input);
                    }
                }
                
            }
        }
        catch (IOException e) {
            image.setEnabled(false);
            inputText.setEnabled(false);
            inputText.setText("*** Deconnecter ***");
        }
    }

    private BufferedReader breader;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField inputText;
    private NetDrawImage image;

}