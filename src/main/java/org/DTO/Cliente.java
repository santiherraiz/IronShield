import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        String host = "";
        int puerto = 1234;

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Intentando conectar con IronShield...");

            if (socket.isConnected()) {
                System.out.println("Conexion acceptada");
            }

        } catch (IOException e) {
            System.err.println("No se pudo conectar");
        }
    }
}