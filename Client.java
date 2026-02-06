import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class Client {
    static final int port = 8080;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java Client <adresse_serveur>");
            return;
        }

        Socket socket = new Socket(args[0], port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        SwingUtilities.invokeLater(() -> {
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