package Prova_LinkedList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Prova {
    final static int PORT = 6666;

    public static void main(String[] args) {

        try  {

            ServerSocket server = new ServerSocket(PORT);
            Socket socket = server.accept();
            System.out.println("Connexió Acceptada");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            out.writeUTF("Connexió Acceptada");

            String msg = "";
            while(!msg.equals("FI")){
                msg = br.readLine();
                out.writeUTF(msg);
                out.flush();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage() + " Error del servidor");
        }
    }
}

