import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    static final int PORT = 6666;
    static ServerSocket server;
    static Socket socket;
    public static volatile AtomicBoolean busy = new AtomicBoolean(false); //Indiquem que el servidor no esta ocupat

    public static void main(String[] args) {

        try {
            //Iniciem server
            server = new ServerSocket(PORT);
            System.out.println("Servidor Iniciat, esperant connexions...");

            //Acceptem un client
            socket = server.accept();
            System.out.println("Client connectat!");

            //Creem canal per informar de l'estat del server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeBoolean(busy.get());

            busy.set(true); //Per tant server ocupat

            //Iniciem funcionalitats d'entrada i sortida
            HandleConnexion handleConnexion = new HandleConnexion(socket);

            //Iniciem thread que controlara connexions no desitjades
            Thread clientHandle = new Thread(new handleNotAllowedClientConnexions(server , busy), "Deny Connexions");

            clientHandle.start();
            handleConnexion.speak();

            clientHandle.join();
            handleConnexion.join();

            busy.set(false); //Alliberem al servidor

        } catch (InterruptedException e) {
            System.out.println("Server error");
        } catch (IOException e) {
            System.out.println("Problemes entrada Sortida: "+e.getMessage());
        }


    }


}




