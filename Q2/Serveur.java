import java.io.*;
import java.net.*;
import java.util.Scanner;

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

        Scanner clavier = new Scanner(System.in);

        // Thread RÉCEPTION (client → écran)
        Thread recevoir = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Client : " + msg);
                    if (msg.equalsIgnoreCase("END")) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread ENVOI (clavier → client)
        Thread envoyer = new Thread(() -> {
            try {
                while (true) {
                    String msg = clavier.nextLine();
                    out.println(msg);
                    if (msg.equalsIgnoreCase("END")) break;
                }
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
        System.out.println("Serveur arrêté");
    }
}