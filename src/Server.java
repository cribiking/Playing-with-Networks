import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    static final int PORT = 6666;
    static ServerSocket server;
    static Socket socket;
    public static AtomicBoolean busy = new AtomicBoolean(false); //Indiquem que el servidor no esta ocupat
    public static AtomicBoolean connected = new AtomicBoolean(true);

    public static void main(String[] args) {

        try {
            //Iniciem server
            server = new ServerSocket(PORT);
            System.out.println("Servidor Iniciat, esperant connexions...");

            //Acceptem un client
            socket = server.accept();

            //Un cop acceptem un client tanquem l'espera de connexions
            server.close();

            System.out.println("Client connectat!");

            busy.set(true); //Per tant server ocupat

            //Iniciem funcionalitats d'entrada i sortida
            HandleConnexion handleConnexion = new HandleConnexion(socket, "Client");
            handleConnexion.waitClientEnd();

        } catch (IOException e) {
            System.out.println("Problemes entrada Sortida: "+e.getMessage());
        } finally {
            busy.set(false); //Alliberem al servidor
            connected.set(false);
        }

    }


}




