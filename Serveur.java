import java.io.*;
import java.net.*;

//Classe principale
public class Serveur {

   //Définission du port sur lequelle on veut que les client passe
   static final int port = 8080;

   public static void main(String[] args) throws Exception {
    //Instanciation avec la classe ServerSocket
     ServerSocket s = new ServerSocket(port);
     Socket soc = s.accept();


        // Un BufferedReader permet de lire par ligne.
        BufferedReader plec = new BufferedReader(
                               new InputStreamReader(soc.getInputStream())
                              );

        // Un PrintWriter possede toutes les operations print classiques.
        // En mode auto-flush, le tampon est vid� (flush) � l appel de println.
        PrintWriter pred = new PrintWriter(
                             new BufferedWriter(
                                new OutputStreamWriter(soc.getOutputStream())),
                             true);

       //Boucle infin                      
        while (true) {
           String str = plec.readLine();          // lecture du message
           if (str.equals("END")) break;
           System.out.println("ECHO = " + str);   // trace locale
           pred.println(str);                     // renvoi d'un echo
        }
        plec.close();
        pred.close();
        soc.close();
   }
}
