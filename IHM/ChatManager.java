import java.io.*;
import java.util.*;

/**
 * Gestionnaire centralisé du chat multi-clients.
 * Gère la liste des clients connectés et diffuse les messages à tous.
 */
public class ChatManager {
    private List<ClientHandler> clients = new ArrayList<>();

    /**
     * Ajoute un nouveau client au chat.
     */
    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        broadcast("[SERVEUR] " + client.getNom() + " a rejoint le chat (" + clients.size() + " clients)");
    }

    /**
     * Supprime un client du chat.
     */
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast("[SERVEUR] " + client.getNom() + " a quitté le chat (" + clients.size() + " clients)");
    }

    /**
     * Diffuse un message à tous les clients connectés.
     */
    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Diffuse un message avec le nom de l'émetteur.
     */
    public synchronized void broadcastFromClient(ClientHandler sender, String message) {
        String fullMessage = "[" + sender.getNom() + "] " + message;
        for (ClientHandler client : clients) {
            client.sendMessage(fullMessage);
        }
    }

    /**
     * Retourne le nombre de clients connectés.
     */
    public synchronized int getClientCount() {
        return clients.size();
    }
}
