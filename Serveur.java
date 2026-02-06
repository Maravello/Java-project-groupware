import java.io.*;
import java.net.*;

public class Serveur {

    static final int port = 8080;

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur en attente sur le port " + port + "...");

        Socket client = serverSocket.accept();
        System.out.println("Client connecté");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream())
        );

        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())
                ), true
        );

        // Thread de réception (ce que le client envoie)
        Thread recevoir = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Reçu du client : " + msg);
                    if (msg.equals("END")) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread d'envoi (ce que le serveur envoie)
        Thread envoyer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    out.println("Message serveur " + i);
                    Thread.sleep(500);
                }
                out.println("END");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        recevoir.start();
        envoyer.start();

        recevoir.join();
        envoyer.join();

        in.close();
        out.close();
        client.close();
        serverSocket.close();

        System.out.println("Serveur terminé");
    }
}