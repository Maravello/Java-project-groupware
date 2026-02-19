
import java.net.*;
import java.io.*;

public class NetDrawServer {

    public NetDrawServer(int port) {
        this.port = port;
    }

    public void launch() {

        ClientList clientList = new ClientList();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Peu pas demarrer le serveur");
            System.out.println("Il y a deja une appli sur ce port " + port + ".");
            System.exit(0);
        }

       // ClientWatcherThread clientWatcherThread = new ClientWatcherThread(clientList);
        //clientWatcherThread.start();

        System.out.println("Port: " + port +"." );

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clientList);
                handler.start();
            }
            catch (IOException e) {
                System.out.println("Somebody jibbled up their connection when connecting.");
            }
            catch (Exception e) {
                System.out.println("Somebody tried to join the server in a jibbly way.");
            }
        }

    }

    private boolean running = true;
    private int port;

}