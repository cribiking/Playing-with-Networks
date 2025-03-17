import java.io.*;
import java.net.Socket;

public class HandleConnexion extends Thread{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader br;
    private boolean isConnected; //Variable control de desconnexió

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
        isConnected =true;
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
            while(isConnected && !socket.isClosed()){//Evitar que listen() es quedi penjat per una desconnexió del socket
                //métode readLine elimina els salts de linea.
                //usant trim() comprovarem si l'String enviat ésta vuit comparant amb isEmpty()
//                if (serverMsg.isEmpty()){//Si el missatge del servidor es diferent a un salt de línea el llegim
                    serverMsg = in.readUTF();
                    System.out.println("[Message]: "+serverMsg);

                    if(serverMsg.equals("FI")){
                        isConnected =false;
                        disconnect();
                    }
//                }
            }
        }catch (IOException e){
            System.out.println("Problema d'entrada de dades: "+e.getMessage());
            disconnect();
        }
    }

    void speak(){
        String msg="";
        try {
            while (isConnected && !socket.isClosed()){
//                if (msg.isEmpty()){//Si el missatge és diferent a un salt de línea vuit, llegim missatge del teclat
                    msg = br.readLine();
                    out.writeUTF(msg);
                    out.flush();

                    if(msg.equals("FI")){
                        isConnected =false;
                        disconnect();
                    }
//                }

            }
        } catch  (IOException e){
            System.out.println("Problemes amb la sortida de dades: "+e.getMessage());
            disconnect();
        }
    }

    void disconnect(){
        isConnected =false;
        try{
            //Prevenció d'errors per si algun element ja ha estat tancat anteriorment per algun problema
            if (socket != null && !socket.isClosed()) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
            if (br != null) br.close();
            System.out.println("Fins Aviat!!");
        } catch (IOException e){
            System.out.println("Error al tancar elements d'entrada/sortida");
        }
    }

    //mèotde creat per assegurar-nos que el fil principal de HandleConnexión ha acabat abans de tancar les connexions.
    void waitClientEnd(){
        try {
            this.join();
        } catch (InterruptedException e){
            System.out.println("Error esperant el final de la connexió: " + e.getMessage());
        }
    }
}
