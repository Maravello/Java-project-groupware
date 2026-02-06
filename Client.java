import java.io.*; //classes liées aux entrées/sorties (Input / Output).
import java.net.*; //sert à tout ce qui touche au réseau 

/** Le processus client se connecte au site fourni dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class Client {
   //instanciation 
   static final int port = 8080;

   public static void main(String[] args) throws Exception {
        Socket socket = new Socket(args[0], port);
        System.out.println("SOCKET = " + socket);

        BufferedReader plec = new BufferedReader(
                               new InputStreamReader(socket.getInputStream())
                               );

        PrintWriter pred = new PrintWriter(
                             new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                             true);

        String str = "bonjour";
        for (int i = 0; i < 10; i++) {
           pred.println(str);        
           str = plec.readLine();      
        }
        
        System.out.println("END");     
        pred.println("END") ;
        plec.close();
        pred.close();
        socket.close();
   }
}
