import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Listener implements Runnable {
    private DataInputStream in;
    private String sender;

    public Listener(DataInputStream in, String sender) {
        this.in = in;
        this.sender = sender;
    }
    @Override
    public void run() {
        try {
            String msg = "";
            while (!msg.equals("Fi")) {
                msg = in.readUTF();
                System.out.println(sender+": "+msg);
            }
        } catch (IOException e) {
            System.out.println("Error communications listener: " + e.getMessage());
        }
    }
}

