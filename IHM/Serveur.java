import java.net.*;

public class Serveur {
    static final int port = 8080;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur multi-clients avec dessin partagé en attente sur le port " + port + "...");
        
        ChatManager chatManager = new ChatManager();
        DrawingManager drawingManager = new DrawingManager();

        // Accepte les clients en boucle infinie
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("Nouveau client connecté : " + client.getInetAddress());

            // Crée un handler pour ce client et le démarre dans un thread séparé
            ClientHandler handler = new ClientHandler(client, chatManager);
            handler.start();
            
            // Crée aussi un handler de dessin partagé
            DrawingClientHandler drawingHandler = new DrawingClientHandler(client, drawingManager);
            drawingHandler.start();
        }
    }
}