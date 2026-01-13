package java.org.DTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime; // Añadido para hora exacta

public class ClienteConex implements Runnable {

    private Socket socket;
    private int idAgente; // Id guardia

    // Constructor
    public ClienteConex(Socket socket, int contadorClientes) {
        this.socket = socket;
        this.idAgente = contadorClientes; // 2. AQUÍ guardamos quién es este hilo
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {

            String mensaje;

            // Log inicial
            System.out.println("[Agente #" + idAgente + "] CONECTADO - Esperando reportes...");

            while ((mensaje = entrada.readLine()) != null) {

                System.out.println("[Agente #" + idAgente + "] Reporta: " + mensaje);


                salida.println("RECIBIDO. Mando Central fuera.");
            }

        } catch (Exception e) {
            System.err.println("[Agente #" + idAgente + "] ERROR: Conexión perdida inesperadamente.");
        } finally {
            try {
                socket.close();
                System.out.println("[Agente #" + idAgente + "] Socket cerrado. Sesión finalizada.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}