import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";


    public static void main(String[] args) {

        Socket socket = null;
        final AtomicBoolean serverIsBusy = new AtomicBoolean();

        try {
            socket = new Socket(HOST, PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());

            //Informació del servidor, si esta o no ocupat
            serverIsBusy.set(in.readBoolean()); //Primer client sera fals

            if (serverIsBusy.get()){
                socket.close();
                System.out.println("Servidor ocupat...");
            } else {
                serverIsBusy.set(true);
                HandleConnexion handleConnexion = new HandleConnexion(socket);
                handleConnexion.waitClientEnd(); //No acabem fins que el servidor acabi

            }


//UnknownHostException
        } catch (ConnectException e) { //Servidor ocupat
             /*
         The client has attempted to connect to a server on a particular IP and port. The connection request has made it to the server machine,
         but there is no service listening for requests on the nominated port. The operating system then "refuses" the connection.
          */
            System.err.println("Servidor ocupat o desconnectat, aquesta aplicació es tancarà: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error d'entrada sortida, es possible que el servidor estigui desconectat: " + e.getMessage());
        }
    }
}

