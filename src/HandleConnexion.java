
import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/*

TODO:
TODO: Superposició de missatges
 */
public class HandleConnexion extends Thread{

    private final String receptor;
    private final boolean isServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader br;
    private static AtomicBoolean isConnected;//Variable control de finalització de programes
    private static final int SLEEP_TIME=250;// Per evitar esperes actives i forçar a anar canviant de fil

    //Constructor de la classe HandleConnexion
    public HandleConnexion(Socket socket, String receptor, boolean isServer) {
        this.isServer = isServer;
        this.receptor = receptor;
        try {
            this.socket = socket;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Inicialització variables entrada i sortida errònia: "+e.getMessage());
        }
        isConnected = new AtomicBoolean(true);
        addShutdownHookThread();
        this.start();
    }

    @Override
    public void run() {

        connexionConfirmation();

        //Iniciem els threads d'escolta i parla
        Thread listenerThread = new Thread(this::listen, "Listener Thread");
        Thread speakerThread = new Thread(this::speak, "Speaker Thread");

        listenerThread.start();
        speakerThread.start();
    }

    private void listen(){
        String entryMsg;
        try{
            //Si ningú ha finalitzat i cap socket ha sigut tancat
            while(isConnected.get() && !socket.isClosed()){

                entryMsg = in.readUTF();

                if (!entryMsg.isEmpty()) {//Si el missatge es diferent a un salt de línea el mostrem
                    System.out.println("[" + receptor + "]: " + entryMsg);
                }
                if (entryMsg.equals("FI")) {
                    isConnected.set(false);
                }

                Thread.sleep(SLEEP_TIME);
            }
        }catch (IOException | InterruptedException e){
            System.out.println("S'ha finalitzat la connexió ");
            //Asegurem tancar les connexions
            disconnect();
            System.exit(0);
        }
    }

    private void speak(){
        String msg = "";

        try {
            //Si ningú ha finalitzat i cap socket ha sigut tancat
            while (isConnected.get() && !socket.isClosed()) {
                //Desde listen, notificarem de que em rebut 'FI' canviant la variable a false, i així br.readLine no bloqueja el thread

                if (br.ready()) { //Per evitar que br.readline() bloquegi el thread, primer revisem si hi ha dades per llegir
                    msg = br.readLine().trim();//Ens asegurem d'eliminar espais en blanc davant i darrera
                }

                out.writeUTF(msg);
                out.flush();

                if (msg.equals("FI")) {
                    isConnected.set(false);
                }

                msg = "";
                Thread.sleep(SLEEP_TIME);
            }

        } catch  (IOException | InterruptedException e){
            //Asegurem tancar les connexions
            disconnect();
            System.exit(0);
        } catch (NullPointerException e){
            System.out.println("Null pointer exception: "+e.getMessage());
        }
    }

    //Mètode per confirmar les connexions del servidor amb client
    private void connexionConfirmation(){
        try {
            if (isServer) {
                System.out.println("Connexió Acceptada");
                out.writeUTF("Connexió Acceptada");
                out.flush();
            } else {
                System.out.println(in.readUTF());
            }
        } catch (IOException e) {
            System.out.println("Problemes al comunicar-se");
        }
    }

    //Mètode per asegurar que tanquem totes les connexions
    private void disconnect(){
        try {
            //Prevenció d'errors per si algun element ja ha estat tancat anteriorment per algun problema
            if (socket != null && !socket.isClosed()) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
            if (br != null) br.close();
        } catch (IOException e) {
            System.out.println("Error al tancar elements d'entrada/sortida");
        }
    }

    //Per a controlar finalitzacions, he afeigit un nou thread personalitzat al JVM shutdowns.
    private void addShutdownHookThread()  {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Avís]: Finalitzant connexió, tancant programa....");

            try {
                //Simulem processos de tancament
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println("Error dormint");
            }

            System.out.println("\n----Connexió Tancada----");
            isConnected.set(false);
        }, "Shutdown Thread"));
    }

}
