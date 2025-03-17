import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    final static int PORT  = 6666;
    public static void main(String[] args) {

    Socket socket = null;

    try (ServerSocket server = new ServerSocket(PORT)) {

        socket = server.accept();
        System.out.println("Client connectat!");
        HandleConnexion handleConnexion = new HandleConnexion(socket);

        handleConnexion.waitClientEnd();//Esperarem a que el fil principal del client acabi abans de tancar el servidor.

    } catch (IOException e){
        System.out.println("Error Server "+e.getMessage());
    } finally {
        try {
            socket.close();
        } catch (IOException e){
            System.out.println("Communications Closes: "+e.getMessage());
        }
    }
         /*
        The client has attempted to connect to a server on a particular IP and port. The connection request has made it to the server machine,
        but there is no service listening for requests on the nominated port. The operating system then "refuses" the connection.
         */
        /*
        recorda que IOExeption recull tant serverExeption com connectionExpetion. IOExpetion es el seu pare
        */
    }
}