import java.io.*;
import java.net.Socket;

public class Client {
    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST , PORT)) {

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readUTF());

            //Volem estar constantment escoltant al servidor
            Thread serverListener = new Thread(new Communications.Listener(in , socket, br), "ServerListener");
            serverListener.start();

            //Volem enviar missatges del Client al Servidor en qualsevol moment
            Thread speakToServer = new Thread(new Communications.Speaker(out , socket), "SpeakToServer");
            speakToServer.start();

            serverListener.join();
            speakToServer.join();

        } catch (IOException | InterruptedException e){
            System.out.println("Error Client "+e.getMessage());
        }
    }
}
