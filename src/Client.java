import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";


    public static void main(String[] args) {

        try {
            Socket socket = new Socket(HOST, PORT);

            //Iniciem funcionalitats d'entrada i sortida
            HandleConnexion handleConnexion = new HandleConnexion(socket , "Server", false);

        } catch (ConnectException e) {
            System.err.println("Servidor ocupat o desconnectat, aquesta aplicació es tancarà...");
        } catch (IOException e) {
            System.err.println("Error d'entrada sortida, es possible que el servidor estigui desconnectat");
        }
    }
}

