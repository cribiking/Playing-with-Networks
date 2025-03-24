import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;


public class Server extends Thread {
    private final int PORT = 6666;
    private ServerSocket server;
    private Socket socket;
    private DataOutputStream out;
    public static volatile AtomicBoolean busy = new AtomicBoolean(false); //Indiquem que el servidor no esta ocupat

    public Server() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Servidor Iniciat, esperant connexions...");

            socket = server.accept();
            System.out.println("Client connectat!");

            busy.set(true);
        } catch (IOException e) {
            System.out.println("Error al constructor de server: " + e.getMessage());
        }
        this.start();
    }

    @Override
    public void run() {
        try {

//          Thread que estarà atent a connexions adicionals
            Thread denyNewConnections = new Thread(this::denyNewConnections, "Thread Deny Connections");
            denyNewConnections.start();
            busy.set(true); // Marcar como ocupado

//          Enviem al client una señal per a que es pugui conectar

            HandleConnexion handleConnexion = new HandleConnexion(socket);
            handleConnexion.waitClientEnd();//Esperem a que el client acabi
            denyNewConnections.join();

            busy.set(false); // Alliberar al servidor

        } catch (InterruptedException e) {
            System.out.println("Problema joins Server");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Communications Closes: " + e.getMessage());
            } catch (NullPointerException e) {
                System.err.println("Servidor en ús.. Intenta-ho més tard. 2");
            }
        }


    }

//    Afegir un thread.sleep o wait per a qu eno consumeixi tanta CPU
    private void denyNewConnections() {

        try {
            while (busy.get()) { //Si busy == true , vol dir que hi ha un client connectat i per tant cancelarem altres connexions
                if (server.isClosed()) {
                    Socket extraClient = server.accept();
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
    public static void main(String[] args) {

        try {
            Server server = new Server();
            server.join();
        } catch (InterruptedException e) {
            System.out.println("Server error");
        }
    }

}




