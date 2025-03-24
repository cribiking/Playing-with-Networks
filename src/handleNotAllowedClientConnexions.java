import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

//Classe utilitzada al servidor per detectar nous clients quan ja n'hi ha un de connectat
public class handleNotAllowedClientConnexions implements Runnable {

    private ServerSocket server;
    private AtomicBoolean busy;

    public handleNotAllowedClientConnexions(ServerSocket server, AtomicBoolean busy) {
        this.server = server;
        this.busy = busy;
    }


    //    Afegir un thread.sleep o wait per a qu eno consumeixi tanta CPU
    @Override
    public void run() {

        try {
            while (busy.get()) { //Si busy == true , vol dir que hi ha un client connectat i per tant cancelarem altres connexions
                Socket extraClient = server.accept();
                if (extraClient.isConnected()) {
                    DataOutputStream out = new DataOutputStream(extraClient.getOutputStream());

                    out.writeBoolean(busy.get());

                    extraClient.close();
                } else {
                    Thread.sleep(50);
                }
            }

        } catch (IOException e) {
            System.err.println("Error en el control de connexions addicionals: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error al posar a dormir al thread: " + e.getMessage());
        }
    }
}
