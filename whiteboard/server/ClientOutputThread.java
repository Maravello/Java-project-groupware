
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientOutputThread extends Thread {
    
    public ClientOutputThread(ClientList clientList, String line) {
        this.clientList = clientList;
        this.line = line;
    }
    
    public void run() {
        ClientHandler[] clients = clientList.getClients();
        for (int i = 0; i < clients.length; i++) {
            ClientHandler clientHandler = clients[i];
            try {
                clientHandler.writeLine(line);
            }
            catch (NullPointerException e) {
                continue;
            }
            catch (IOException e) {
                clientList.delete(clientHandler);
                continue;
            }
        }
    }
    
    private ClientList clientList = null;
    private String line = null;
    
}