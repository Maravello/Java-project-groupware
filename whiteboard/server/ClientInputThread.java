
import java.io.*;
import java.net.*;

public class ClientInputThread extends Thread {
    
    public ClientInputThread(BufferedReader breader, ClientList clientList, ClientHandler clientHandler) {
        this.breader = breader;
        this.clientList = clientList;
        this.clientHandler = clientHandler;
    }
    
    public void run() {
        String line = null;
        boolean running = true;
        while (running) {
            try {
                line = breader.readLine();
            }
            catch (IOException e) {
                clientList.delete(clientHandler);
                return;
            }
            if (line == null) {
                clientList.delete(clientHandler);
                return;
            }
            if (line.equals("quit")) {
                clientList.delete(clientHandler);
                return;
            }
            ClientOutputThread clientOutputThread = new ClientOutputThread(clientList, line);
            clientOutputThread.start();
        }
    }
    
    private BufferedReader breader = null;
    private ClientList clientList = null;
    private ClientHandler clientHandler = null;
    
}