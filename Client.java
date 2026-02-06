import java.io.*;
import java.net.*;

public class Serveur {

    static final int port = 8080;

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur en attente...");

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

        client.close();
        serverSocket.close();
        System.out.println("Serveur terminé");
    }
}