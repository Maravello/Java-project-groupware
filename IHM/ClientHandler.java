import java.io.*;
import java.net.*;

/**
 * Gère la connexion d'un client individuel.
 * S'exécute dans un thread séparé pour chaque client.
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ChatManager chatManager;
    private String nom;
    private static int clientCounter = 0;

    public ClientHandler(Socket socket, ChatManager chatManager) throws IOException {
        this.socket = socket;
        this.chatManager = chatManager;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        
        // Génère un nom unique pour le client
        synchronized (ClientHandler.class) {
            this.nom = "Client_" + (++clientCounter);
        }
    }

    @Override
    public void run() {
        try {
            // Ajoute le client au gestionnaire
            chatManager.addClient(this);
            
            // Lit et diffuse les messages
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("END")) {
                    break;
                }
                // Diffuse le message à tous les clients
                chatManager.broadcastFromClient(this, msg);
            }
        } catch (IOException e) {
            System.err.println("Erreur avec " + nom + ": " + e.getMessage());
        } finally {
            // Nettoie la connexion
            chatManager.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(nom + " déconnecté");
        }
    }

    /**
     * Envoie un message au client.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Retourne le nom du client.
     */
    public String getNom() {
        return nom;
    }
}
