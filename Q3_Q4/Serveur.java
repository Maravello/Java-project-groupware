import java.net.*;

public class Serveur {
    static final int chatPort = 8080;
    static final int drawingPort = 8081;

    public static void main(String[] args) throws Exception {
        ChatManager chatManager = new ChatManager();
        DrawingManager drawingManager = new DrawingManager();

        // Thread pour accepter les connexions de chat
        Thread chatThread = new Thread(() -> {
            try {
                ServerSocket chatSocket = new ServerSocket(chatPort);
                System.out.println("Serveur CHAT en attente sur le port " + chatPort + "...");
                
                while (true) {
                    Socket client = chatSocket.accept();
                    System.out.println("[CHAT] Nouvelle connexion : " + client.getInetAddress());
                    ClientHandler handler = new ClientHandler(client, chatManager);
                    handler.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread pour accepter les connexions de dessin
        Thread drawingThread = new Thread(() -> {
            try {
                ServerSocket drawingSocket = new ServerSocket(drawingPort);
                System.out.println("Serveur DESSIN en attente sur le port " + drawingPort + "...");
                
                while (true) {
                    Socket client = drawingSocket.accept();
                    System.out.println("[DESSIN] Nouvelle connexion : " + client.getInetAddress());
                    DrawingClientHandler handler = new DrawingClientHandler(client, drawingManager);
                    handler.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        chatThread.setDaemon(false);
        drawingThread.setDaemon(false);
        chatThread.start();
        drawingThread.start();
        
        System.out.println("Serveur PRÊT : Chat port " + chatPort + ", Dessin port " + drawingPort);
    }
}