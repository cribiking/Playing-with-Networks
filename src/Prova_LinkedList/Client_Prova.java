package Prova_LinkedList;

import java.io.*;
import java.net.Socket;

public class Client_Prova {

    final static int PORT = 6666;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Socket socket = null;
        try  {

            socket = new Socket(HOST , PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readUTF());

            //Volem estar constantment escoltant al servidor

            Thread serverListener = new Thread(new Commnication_PRova.Listener(in , socket, br), "ServerListener");
            serverListener.start();
            serverListener.join();

        } catch (IOException | InterruptedException e){
            System.out.println("Error Client");
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                System.out.println("Error en el Cliente: " + e.getMessage());
            }
        }
    }
}
