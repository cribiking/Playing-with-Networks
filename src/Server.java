import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    static final int PORT = 6666;
    static ServerSocket server;
    static Socket socket;

    public static void main(String[] args) {

        try {
            //Iniciem servidor
            server = new ServerSocket(PORT);
            System.out.println("Servidor Iniciat, esperant connexions...");

            //Acceptem un client
            socket = server.accept();
            System.out.println("Client connectat!");

            //Un cop acceptem un client tanquem l'espera de connexions
            server.close();

            //Iniciem funcionalitats d'entrada i sortida
            HandleConnexion handleConnexion = new HandleConnexion(socket, "Client", true);

        } catch (IOException e) {
            System.out.println("Servidor ja ha sigut iniciat");
        }

    }

}




