import java.io.*;
import java.net.Socket;

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

        } catch (IOException | InterruptedException e) {
            System.out.println("Error Client " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e ) {
                System.out.println("Communications Closes: " + e.getMessage());
            }
        }
    }
}
