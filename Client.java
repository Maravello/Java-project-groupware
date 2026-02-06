import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    static final int port = 8080;

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Usage : java Client <adresse_serveur>");
            return;
        }

        Socket socket = new Socket(args[0], port);
        System.out.println("Connecté au serveur");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                ), true
        );

        Scanner clavier = new Scanner(System.in);

        // Thread ENVOI (clavier → serveur)
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

        // Thread RÉCEPTION (serveur → écran)
        Thread recevoir = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Serveur : " + msg);
                    if (msg.equalsIgnoreCase("END")) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        envoyer.start();
        recevoir.start();

        envoyer.join();
        recevoir.join();

        socket.close();
        System.out.println("Client déconnecté");
    }
}