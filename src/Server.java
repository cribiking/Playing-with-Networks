import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;


public class Server {

    final static int PORT  = 6666;
    public static void main(String[] args) {

        final AtomicBoolean bussy = new AtomicBoolean(false);
        Socket socket = null;

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("Servidor Iniciat, esperant connexions...");

            socket = server.accept();
            Thread serverBusy = new Thread(()->{
                //rellenar aqui usando solo las librerias del paquete java
            });

            System.out.println("Client connectat!");
            HandleConnexion handleConnexion = new HandleConnexion(socket);

            handleConnexion.waitClientEnd();//Esperarem a que el fil principal del client acabi abans de tancar el servidor.

        } catch (IOException e) {
            System.out.println("Error Server: " + e.getMessage());

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Communications Closes: " + e.getMessage());
            } catch (NullPointerException e) {
                System.err.println("Servidor en ús.. Intenta-ho més tard. 2");
            }
        }
    }
}



