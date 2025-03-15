import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    final static int PORT  = 6666;
    public static void main(String[] args) {

    try (ServerSocket server = new ServerSocket(PORT)) {

        Socket socket = server.accept();
        System.out.println("Connexió Acceptada");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        out.writeUTF("Connexió Acceptada");

        //Volem estar constantment escoltant al client
        Thread clientListener = new Thread(new Communications.Listener(in , socket, br), "ClientListener");
        clientListener.start();

        //Volem enviar missatges del Servidor al client en qualsevol moment
        Thread speakToClient = new Thread(new Communications.Speaker(out, socket), "SpeakToClient");
        speakToClient.start();

        clientListener.join();
        speakToClient.join();

    } catch (IOException | InterruptedException e){
        System.out.println("Error Server "+e.getMessage());
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