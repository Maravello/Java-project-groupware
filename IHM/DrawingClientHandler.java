import java.io.*;
import java.net.*;

/**
 * Gère la connexion d'un client pour le dessin partagé.
 * Reçoit les commandes de dessin et les diffuse via DrawingManager.
 */
public class DrawingClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DrawingManager drawingManager;
    private String clientId;
    private int clientColor;
    private static int clientCounter = 0;
    
    public DrawingClientHandler(Socket socket, DrawingManager drawingManager) throws IOException {
        this.socket = socket;
        this.drawingManager = drawingManager;
        
        // L'ordre est important : ObjectOutputStream doit être créé avant ObjectInputStream
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
        
        synchronized (DrawingClientHandler.class) {
            this.clientId = "Client_" + (++clientCounter);
        }
    }
    
    @Override
    public void run() {
        try {
            // Ajoute le client au gestionnaire
            drawingManager.addDrawingClient(this);
            
            // Lit et diffuse les commandes de dessin
            Object obj;
            while ((obj = in.readObject()) != null) {
                if (obj instanceof DrawingCommand) {
                    DrawingCommand cmd = (DrawingCommand) obj;
                    cmd = new DrawingCommand(cmd.getType(), cmd.getX1(), cmd.getY1(), 
                                           cmd.getX2(), cmd.getY2(), cmd.getColor(), clientId, clientColor);
                    drawingManager.broadcastDrawingCommand(cmd);
                    System.out.println("[DrawingClientHandler] Commande reçue: " + cmd);
                }
            }
        } catch (EOFException e) {
            System.out.println("[DrawingClientHandler] " + clientId + " déconnecté");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DrawingClientHandler] Erreur avec " + clientId + ": " + e.getMessage());
        } finally {
            drawingManager.removeDrawingClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Envoie une commande de dessin au client.
     */
    public synchronized void sendDrawingCommand(DrawingCommand command) {
        try {
            out.writeObject(command);
            out.flush();
        } catch (IOException e) {
            System.err.println("[DrawingClientHandler] Erreur d'envoi à " + clientId + ": " + e.getMessage());
        }
    }
    
    /**
     * Retourne l'ID du client.
     */
    public String getClientId() {
        return clientId;
    }
    
    /**
     * Définit la couleur du client.
     */
    public void setClientColor(int color) {
        this.clientColor = color;
    }
    
    /**
     * Retourne la couleur du client.
     */
    public int getClientColor() {
        return clientColor;
    }
}
