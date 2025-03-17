import java.io.*;
import java.net.Socket;

public class HandleConnexion extends Thread{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader br;
    //Variable de control de desconexió
    private boolean isConescted;

    public HandleConnexion(Socket socket) {
        this.socket = socket;
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Inicialització variables entrada i sortida errònia: "+e.getMessage());
            throw new RuntimeException(e);
        }
        isConescted=true;
        this.start();
    }


    @Override
    public void run() {

        try {
            //Fer threads
            Thread listenerThread = new Thread(this::listen, "Listener Thread");
            Thread speakerThread = new Thread(this::speak , "Speaker Thread");

            listenerThread.start();
            speakerThread.start();

            listenerThread.join();
            speakerThread.join();

        } catch (InterruptedException e){ //joins
            System.out.println("Problemes amb concurrencia dels threads: "+e.getMessage());
        } finally {
            disconnect();
        }

    }

    void listen(){
        String serverMsg = "";
        try{
            while(isConescted){
                serverMsg = in.readUTF();
                System.out.println("[Message]: "+serverMsg);

                if(serverMsg.equals("FI")){
                    isConescted=false;
                    //cal un brake??
                }
            }
        }catch (IOException e){
            System.out.println("Problema d'entrada de dades: "+e.getMessage());
        }
    }

    void speak(){
        String msg="";
        try {
            while (isConescted){
                msg = br.readLine();
                out.writeUTF(msg);
                out.flush();

                if(msg.equals("FI")){
                    isConescted=false;

                }
            }
        } catch  (IOException e){
            System.out.println("Problemes amb la sortida de dades: "+e.getMessage());

        }
    }

    void disconnect(){
        try{
            isConescted=false;
            socket.close();
            out.close();
            in.close();
            br.close();
            System.out.println("Fins Aviat!!");
        } catch (IOException e){
            System.out.println("Error al tancar elements d'entrada/sortida");
        }
    }
}
