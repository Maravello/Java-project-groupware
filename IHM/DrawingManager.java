import java.util.*;

/**
 * Gestionnaire centralisé pour le dessin partagé.
 * Enregistre les clients et diffuse les commandes de dessin.
 */
public class DrawingManager {
    private List<DrawingClientHandler> clients = new ArrayList<>();
    private List<DrawingCommand> drawingHistory = new ArrayList<>();
    
    /**
     * Ajoute un client au gestionnaire de dessin.
     */
    public synchronized void addDrawingClient(DrawingClientHandler client) {
        clients.add(client);
        System.out.println("[DrawingManager] Client ajouté: " + client.getClientId() + 
                          " (total: " + clients.size() + ")");
        
        // Envoie l'historique du dessin au nouveau client
        for (DrawingCommand cmd : drawingHistory) {
            client.sendDrawingCommand(cmd);
        }
    }
    
    /**
     * Supprime un client du gestionnaire.
     */
    public synchronized void removeDrawingClient(DrawingClientHandler client) {
        clients.remove(client);
        System.out.println("[DrawingManager] Client supprimé: " + client.getClientId() + 
                          " (total: " + clients.size() + ")");
    }
    
    /**
     * Diffuse une commande de dessin à tous les clients.
     */
    public synchronized void broadcastDrawingCommand(DrawingCommand command) {
        // Enregistre dans l'historique
        drawingHistory.add(command);
        
        // Diffuse à tous les clients
        for (DrawingClientHandler client : clients) {
            client.sendDrawingCommand(command);
        }
    }
    
    /**
     * Efface l'historique du dessin.
     */
    public synchronized void clearDrawing() {
        drawingHistory.clear();
        DrawingCommand clearCmd = new DrawingCommand(DrawingCommand.Type.CLEAR, "SYSTEM");
        for (DrawingClientHandler client : clients) {
            client.sendDrawingCommand(clearCmd);
        }
    }
    
    /**
     * Retourne le nombre de clients connectés.
     */
    public synchronized int getClientCount() {
        return clients.size();
    }
}
