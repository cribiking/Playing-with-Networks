import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
//pene
public class Client {

    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readUTF());

            //Volem estar constantment escoltant al servidor
            Thread serverListener = new Thread(new Listener(in, "Server"), "ServerListener");
            System.out.println(serverListener);
            serverListener.start();

            //Volem enviar missatges del Client al Servidor en qualsevol moment
            Thread speakToServer = new Thread(new Speaker(out, br), "SpeakToServer");
            System.out.println(speakToServer);
            speakToServer.start();

            serverListener.join();
            speakToServer.join();

            socket.close();
        } catch (InterruptedException e) { //joins
            System.out.println("Error Client " + e.getMessage());
        } catch (UnknownHostException e) { //Servidor ocupat
             /*
         The client has attempted to connect to a server on a particular IP and port. The connection request has made it to the server machine,
         but there is no service listening for requests on the nominated port. The operating system then "refuses" the connection.
          */
            System.out.println("Servidor ocupat o desconectat, aquesta aplicació es tancarà: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error d'entrada sortida, es possible que el servidor estigui desconectat: " + e.getMessage());
        }
    }
}

