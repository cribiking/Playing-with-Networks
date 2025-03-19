import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;


public class Server  {

    final static int PORT = 6666;

    public static void main(String[] args) {

        final AtomicBoolean bussy = new AtomicBoolean(false);
        Socket socket = null;

        try (ServerSocket server = new ServerSocket(PORT)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Informem al client que no hi ha ningu connectat
            out.writeBoolean(bussy.get());
            out.flush();

            System.out.println("Servidor Iniciat, esperant connexions...");
            while (true) {

                if(bussy.get()){ //si ja hi ha un client connectat
                    out.writeUTF("Servidor Ocupado. Inténtelo más tarde.");
                    socket.close();

                } else {
                    //Acpetem la connexió amb nmés 1 client
                    socket = server.accept();
                    System.out.println("Client connectat!");

                    //Notifiquem que hi ha un client connectat
                    bussy.set(true);
                    out.writeBoolean(bussy.get());

                    //Iniciem thread per controlar les connexions
                    HandleConnexion handleConnexion = new HandleConnexion(socket);
                    handleConnexion.waitClientEnd();//Esperarem a que el fil principal del client acabi abans de tancar el servidor.
                }
            }

        } catch (IOException e) {
            System.out.println("Error Server: " + e.getMessage());

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Communications Closes: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Servidor en ús.. Intenta-ho més tard. 2");
            }
        }
    }
}



