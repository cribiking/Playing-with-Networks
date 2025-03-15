import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.RuntimeMXBean;
import java.net.Socket;

public class Speaker implements Runnable {
    private DataOutputStream out;

    private BufferedReader br;

    public Speaker(DataOutputStream out,  BufferedReader br) {
        this.out = out;
        this.br = br;
    }
    @Override
    public void run() {
        try {
            String msg = "";
            while (!msg.equals("FI")){
                msg = br.readLine();
                out.writeUTF(msg);
                out.flush();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
