import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Communications {

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
//                in = new DataInputStream(socket.getInputStream());
                while (!message.equals("FI")){
                    message = br.readLine();
                    System.out.println("Communication Listener recive: "+message);
                }

            } catch (IOException e){
                System.out.println("Error communications listener: "+e.getMessage());
            } finally {
                try {
                    socket.close();
                    br.close();
                } catch (IOException e){
                    System.out.println("Communications Closes: "+e.getMessage());
                }
            }
        }
    }

    public static class Speaker implements Runnable {
        private DataOutputStream out;
        private Socket socket;
        public Speaker(DataOutputStream out, Socket socket) {
            this.out = out;
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                while (!message.equals("FI")){
                    out.writeUTF(message);
                    out.flush();
                }
            } catch (IOException e){
                System.out.println(e.getMessage());
            } finally {
                try {
                    out.close();
                    socket.close();
                } catch (IOException e){
                    System.out.println("Communications Closes: "+e.getMessage());
                }
            }
        }
    }

}
