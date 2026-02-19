
import java.util.*;

public class ClientList {
    
    public ClientList() {
        
    }
    
    public synchronized void add(ClientHandler client) {
        synchronized (list) {
            list.add(client);
        }
        System.out.println("* " + client.getClientName() + " Viens de rejoindre le serveur.");
        ClientOutputThread clientOutputThread = new ClientOutputThread(this, "alert " + client.getClientName() + " just joined the server.");
        clientOutputThread.start();
    }
    
    public synchronized void delete(ClientHandler client) {
        client.killThreads();
        System.out.println("* " + client.getClientName() + " Viens de quitter le serveur.");
        synchronized (list) {
            list.remove(client);
        }
        ClientOutputThread clientOutputThread = new ClientOutputThread(this, "alert " + client.getClientName() + " Viens de quitter le serveur.");
        clientOutputThread.start();
    }
    
    public ClientHandler[] getClients() {
        ClientHandler[] clients = new ClientHandler[0];
        synchronized (list) {
            clients = new ClientHandler[list.size()];
            Iterator it = list.iterator();
            for (int i = 0; i < list.size(); i++) {
                clients[i] = (ClientHandler)it.next();
            }
        }
        return clients;
    }
    
    public void printAllClients() {
        System.out.println("Client connecte: ");
        Iterator clientIt = list.iterator();
        while (clientIt.hasNext()) {
            System.out.print(" " + ((ClientHandler)clientIt.next()).getClientName());
        }
        System.out.println();
    }
    
    private List list = Collections.synchronizedList(new LinkedList());
    
}