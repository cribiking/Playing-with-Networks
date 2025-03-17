import java.io.*;
import java.net.Socket;

public class HandleConnexion extends Thread{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader br;
    private boolean isConnected; //Variable control de finalització de programes
    private boolean processData; //Variable utilitzada per gestionar el temps d'ús de la cpu. Si un
    private static final int SLEEP_TIME=50;// Per evitar esperes actives i forçar a anar canviant de fil
    private final Object lock = new Object(); // Objecte de syncronització compartit

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
        processData=true;
        this.start();
    }

/*
The Sender is supposed to send a data packet to the Receiver.
The Receiver cannot process the data packet until the Sender finishes sending it.
Similarly, the Sender shouldn’t attempt to send another packet unless the Receiver has already processed the previous packet.
 */
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
        String serverMsg;
        try{
            //Si ningú ha finalitzat el xatín, cap socket ha sigut tancat, i em sigut notificats per escoltar:
            while(isConnected && !socket.isClosed()){

                synchronized (lock){
                    while (processData){
                        lock.wait();
                    }
                }

                if (in.available() > 0){//Si hi ha bits per llegir
                    serverMsg = in.readUTF();
                    if (!serverMsg.isEmpty()){//Si el missatge es diferent a un salt de línea el mostrem
                        System.out.println("[Message]: "+serverMsg);
                    }
                    if (serverMsg.equals("FI")) {
                        isConnected = false;
                        disconnect();
                    }
                }
//                else {//Si no hi ha entrada de bits, pausem el thread fins que n'hi torni a haver
//                    Thread.sleep(SLEEP_TIME);
//                }

                synchronized (lock){
                    processData = true;//Avisem que les dades han sigut processades
                    lock.notifyAll();//Avisem a speak que ja em finalitzat
                }
            }
        }catch (IOException | InterruptedException e){
            System.out.println("Problema d'entrada de dades: "+e.getMessage());
            Thread.currentThread().interrupt();
            System.err.println("Thread listen Interrupted");
            disconnect();
        }
    }

    void speak(){
        String msg;
        try {
            //Si ningú ha finalitzat el xatín, cap socket ha sigut tancat i la senyal de que ja s'ha rebut la informació és positiva:
            while (isConnected && !socket.isClosed()){

                synchronized (lock){
                    while (!processData){
                        lock.wait();
                    }
                }

                msg = br.readLine().trim();//Ens asegurem d'eliminar espais en blanc davant i darrera
                if (!msg.isEmpty()){//Si el missatge és diferent a un salt de línea, enviem el missatge.
                    out.writeUTF(msg);
                    out.flush();
                }
                if(msg.equals("FI")){
                    isConnected =false;
                    disconnect();
                }

                synchronized (lock){
                    processData = false;//indiquem que el missatge ha sigut enviat
                    lock.notifyAll();//notifiquem al thread listen de que ja pot començar a llegir dades
                }
            }

        } catch  (IOException | InterruptedException e ){
            System.out.println("Problemes amb la sortida de dades: "+e.getMessage());
            Thread.currentThread().interrupt();
            System.err.println("Thread speak Interrupted");
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
