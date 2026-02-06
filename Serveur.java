import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class Serveur {
    static final int port = 8080;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur en attente...");
        Socket client = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

        // Lancement de l'IHM
        SwingUtilities.invokeLater(() -> {
            FenBoutonsDyn gui = new FenBoutonsDyn(out, in);
            gui.setVisible(true);
            
            // Thread de réception qui met à jour l'IHM
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        final String finalMsg = msg;
                        SwingUtilities.invokeLater(() -> gui.diffuserMessage(finalMsg));
                        if (msg.equalsIgnoreCase("END")) break;
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }).start();
        });
    }
}