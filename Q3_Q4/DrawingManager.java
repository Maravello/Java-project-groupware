import java.util.*;
import java.awt.Color;

/**
 * Gestionnaire centralisé pour le dessin partagé.
 * Enregistre les clients et diffuse les commandes de dessin.
 * Attribue une couleur unique à chaque client.
 */
public class DrawingManager {
    private List<DrawingClientHandler> clients = new ArrayList<>();
    private List<DrawingCommand> drawingHistory = new ArrayList<>();
    private Map<String, Integer> clientColors = new HashMap<>();
    private int[] colors = {
        Color.RED.getRGB(),
        Color.BLUE.getRGB(),
        Color.GREEN.getRGB(),
        Color.ORANGE.getRGB(),
        Color.MAGENTA.getRGB(),
        new Color(255, 0, 127).getRGB(),  // Rose
        new Color(0, 255, 255).getRGB(),  // Cyan
        new Color(128, 0, 128).getRGB(),  // Violet
        new Color(165, 42, 42).getRGB(),  // Marron
        new Color(0, 128, 128).getRGB()   // Teal
    };
    private int colorIndex = 0;
    
    /**
     * Ajoute un client au gestionnaire de dessin.
     */
    public synchronized void addDrawingClient(DrawingClientHandler client) {
        clients.add(client);
        
        // Attribue une couleur unique au client
        int clientColor = colors[colorIndex % colors.length];
        clientColors.put(client.getClientId(), clientColor);
        client.setClientColor(clientColor);
        colorIndex++;
        
        System.out.println("[DrawingManager] Client ajouté: " + client.getClientId() + 
                          " Couleur: " + Integer.toHexString(clientColor) + 
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
        clientColors.remove(client.getClientId());
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
    
    /**
     * Retourne la couleur attribuée à un client.
     */
    public synchronized Integer getClientColor(String clientId) {
        return clientColors.get(clientId);
    }
}
