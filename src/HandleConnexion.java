
import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/*
TODO: CTRL-C controlat, falta avisar al altre codi de que acabi.
TODO: Si un altre client es vol connectar i el servidor esta ocupat, avisar que el server esta ocupat
TODO: Superposició de missatges
 */
public class HandleConnexion extends Thread{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader br;
    private static volatile AtomicBoolean isConnected;//Variable control de finalització de programes
    public static volatile AtomicBoolean notifyEnd;
    private static final int SLEEP_TIME=100;// Per evitar esperes actives i forçar a anar canviant de fil

    public HandleConnexion(Socket socket) {
        try {
            this.socket = socket;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Inicialització variables entrada i sortida errònia: "+e.getMessage());
        }
        isConnected = new AtomicBoolean(true);
        notifyEnd = new AtomicBoolean(false);
        addShutdownHookThread();
        this.start();
    }

    @Override
    public void run() {

        try {
            System.out.println("Connexió Acceptada");

            Thread listenerThread = new Thread(this::listen, "Listener Thread");
            Thread speakerThread = new Thread(this::speak , "Speaker Thread");

            listenerThread.start();
            speakerThread.start();

            listenerThread.join();
            speakerThread.join();


        } catch (InterruptedException e){ //joins
            System.out.println("Problemes amb concurrencia dels threads: "+e.getMessage());
        }
        finally {
            disconnect();
        }
    }

    void listen(){
        String entryMsg;
        try{
            //Si ningú ha finalitzat el xatín, cap socket ha sigut tancat, i em sigut notificats per escoltar:
            while(isConnected.get() && !socket.isClosed()){

                if (in.available() > 0){//Si hi ha bits per llegir
                    entryMsg = in.readUTF();

                    if (!entryMsg.isEmpty()){//Si el missatge es diferent a un salt de línea el mostrem
                        System.out.println("[Message]: " + entryMsg);
                    }
                    if (entryMsg.equals("FI")) {
                        isConnected.set(false);
                    }
                }
                else {//Si no hi ha entrada de bits, pausem el thread fins que n'hi torni a haver
                    Thread.sleep(SLEEP_TIME);
                }
            }
        }catch (IOException | InterruptedException e){
            System.out.println("Problema d'entrada de dades: "+e.getMessage());
            Thread.currentThread().interrupt();
            System.err.println("Thread listen Interrupted");
        }
    }

    void speak(){
        String msg;
        try {

            //Si ningú ha finalitzat el xatín, cap socket ha sigut tancat i la senyal de que ja s'ha rebut la informació és positiva:
            while (isConnected.get() && !socket.isClosed()) {
                //Desde listen, notificarem de que em rebut 'FI' canviant la variable a false, i així br.readLine no bloqueja el thread

                if (br.ready()) { //Per evitar que br.readline() bloquegi el thread, primer revisem si hi ha dades per llegir
                    msg = br.readLine().trim();//Ens asegurem d'eliminar espais en blanc davant i darrera

                    if (!msg.isEmpty()) {//Si el missatge és diferent a un salt de línea, enviem el missatge.
                        out.writeUTF(msg);
                        out.flush();
                    }

                    if (msg.equals("FI")) {
                        isConnected.set(false);
                    }
                } else {
                    Thread.sleep(SLEEP_TIME);
                }
            }
        } catch  (IOException e ){
            System.out.println("Problemes amb la sortida de dades: "+e.getMessage());
            System.exit(0);
            System.err.println("Thread speak Interrupted");
        } catch (NullPointerException e){
            System.out.println("Null pointer exception: "+e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Thread speak Interrupted");
        }

    }

    void disconnect(){
        isConnected.set(false);
        try {
            //Prevenció d'errors per si algun element ja ha estat tancat anteriorment per algun problema
            if (socket != null && !socket.isClosed()) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
            if (br != null) br.close();
            System.out.println("Fins Aviat!!");
        } catch (IOException e) {
            System.out.println("Error al tancar elements d'entrada/sortida");
        }
    }

    //mèotde creat per assegurar-nos que el fil principal de HandleConnexión ha acabat abans de tancar les connexions.
    //Creada per codi més net
    void waitClientEnd(){
        try {
            this.join();
        } catch (InterruptedException e){
            System.out.println("Error esperant el final de la connexió: " + e.getMessage());
        }
    }

/*
El prolema per el qual no podem tancar el thread de l'altre extrem es perquè no podem enviar
cap missatge de finalització per un socket ja tancat. Quan s'executa el Shutdown es perque el programa
s'ha tancat.
 */
    void addShutdownHookThread()  {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nConnexió Tancada...");
            isConnected.set(false);
            notifyEnd.set(true);
        }, "Shutdown Thread"));
    }

}
