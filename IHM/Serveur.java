import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {
    static final int port = 8080;
    // Liste pour stocker les flux de sortie de tous les clients connectés
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur multi-client lancé sur le port " + port);

        try {
            while (true) {
                // Accepte une nouvelle connexion sans bloquer les autres
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress());
                
                // Lance un thread dédié pour ce client
                new ClientHandler(clientSocket).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    // Classe interne pour gérer chaque client individuellement
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // On ajoute ce client à la liste globale
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Message reçu : " + msg);
                    // Rediffusion du message à TOUS les clients
                    broadcast(msg);
                    if (msg.equalsIgnoreCase("END")) break;
                }
            } catch (IOException e) {
                System.out.println("Erreur client : " + e.getMessage());
            } finally {
                // Nettoyage à la déconnexion
                try {
                    synchronized (clientWriters) { clientWriters.remove(out); }
                    socket.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }

        // Envoie le message à tout le monde
        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}