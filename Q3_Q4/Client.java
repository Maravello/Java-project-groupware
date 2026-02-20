import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class Client {
    static final int chatPort = 8080;
    static final int drawingPort = 8081;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java Client <adresse_serveur>");
            return;
        }

        String serverAddr = args[0];

        // Connexion 1 : Chat sur port 8080
        Socket chatSocket = new Socket(serverAddr, chatPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream())), true);
        System.out.println("Connecté au serveur de chat sur " + serverAddr + ":" + chatPort);

        // Connexion 2 : Dessin sur port 8081
        Socket drawingSocket = new Socket(serverAddr, drawingPort);
        ObjectOutputStream objOut = new ObjectOutputStream(drawingSocket.getOutputStream());
        objOut.flush();
        ObjectInputStream objIn = new ObjectInputStream(drawingSocket.getInputStream());
        System.out.println("Connecté au serveur de dessin sur " + serverAddr + ":" + drawingPort);

        SwingUtilities.invokeLater(() -> {
            // Lance la fenêtre de dessin partagée
            FenDessinPartagee dessinWindow = new FenDessinPartagee(objOut, objIn);
            dessinWindow.setVisible(true);

            // Lance aussi la fenêtre de chat
            FenBoutonsDyn gui = new FenBoutonsDyn(out, in);
            gui.setVisible(true);

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