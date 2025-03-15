import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.management.RuntimeMXBean;
import java.net.Socket;

public class Speaker implements Runnable {
    private DataOutputStream out;
    private Socket socket;
    public Speaker(DataOutputStream out, Socket socket) {
        this.out = out;
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            while (){

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
