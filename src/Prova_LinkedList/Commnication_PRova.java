package Prova_LinkedList;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class Commnication_PRova {

    private static String message = "";

    /*
    classe encarregada d'Escoltar
     */
    public static class Listener implements Runnable {

        private DataInputStream in;
        private BufferedReader br;
        private Socket socket;

        public Listener(DataInputStream in, Socket socket, BufferedReader br) {
            this.in = in;
            this.socket = socket;
            this.br = br;
        }

        @Override
        public void run() {
            /*
            Com que estem utilitzant una variable per controlar qui envia i qui rep els missatges, si un usuari escriu diversos missatges a l'hora
            abans de llesgir el seguent, l'haurem d'enviar.
             */
            try {
                LinkedList<String> list = new LinkedList<>();
                while (!message.equals("FI")) {
                    System.out.println("Entra al while??");
                    message = in.readUTF();
                    System.out.println("Arriba aqui?");
                    list.add(message);
                    System.out.println("Communication Listener recive: " + message);
                }

                for (String s : list) {
                    System.out.print(s + " ");
                }

            } catch (IOException e) {
                System.out.println("Error communications listener: " + e.getMessage());
            } finally {
                try {
                    System.out.println("Cerrando conexión...");
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el socket: " + e.getMessage());
                }
            }
        }
    }
}
