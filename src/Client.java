import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";
    private static Socket socket;

    public Client(){
        try {
            this.socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        try {

            Client c1 = new Client();
            HandleConnexion handleConnexion = new HandleConnexion(socket);

            handleConnexion.waitClientEnd(); //No acabem fins que el servidor acabi

        } catch (UnknownHostException e) { //Servidor ocupat
             /*
         The client has attempted to connect to a server on a particular IP and port. The connection request has made it to the server machine,
         but there is no service listening for requests on the nominated port. The operating system then "refuses" the connection.
          */
            System.out.println("Servidor ocupat o desconectat, aquesta aplicació es tancarà: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error d'entrada sortida, es possible que el servidor estigui desconectat: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Socket client error: "+e.getMessage());
            }
        }
    }
}

