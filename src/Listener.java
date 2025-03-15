import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Listener implements Runnable {
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
            while () {

            }
        } catch (IOException e) {
            System.out.println("Error communications listener: " + e.getMessage());
        } finally {
            try {
                socket.close();
                br.close();
            } catch (IOException e) {
                System.out.println("Communications Closes: " + e.getMessage());
            }
        }
    }
}

