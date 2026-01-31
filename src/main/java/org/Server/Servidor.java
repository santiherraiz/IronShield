package org.Server;

import org.DTO.ClientHandler;
import org.DTO.InactivityMonitor;
    
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La clase {@link Servidor} es la que se encarga de las conexiones TCP con los clientes. 
 * Este genera un hilo por cada conexión. El objetivo es recibir o enviar el JSON pertinente al cliente. 
 * Por ejemplo, queremos recibir un JSON con los datos de los agentes, recibiremos 1 JSON con los datos del agente. 
 * Nunca se enviarán más de 1 JSON.
 * NUEVO: Ahora también inicia el monitor de inactividad para detectar guardias inactivos automáticamente.
 *
 */
public class Servidor {
    private static final int PORT = UtilsServer.getServerPort("server.properties");
    private static final String HOST = UtilsServer.getServerName("server.properties");

    public static void main(String[] args) {
        System.out.printf("Servidor iniciado en %s:%d\n", HOST, PORT);
        
        // Iniciar el monitor de inactividad
        try {
            InactivityMonitor.start();
        } catch (Exception e) {
            UtilsServer.writeServerLog("Error al iniciar el monitor de inactividad: " + e.getMessage());
        }

        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    final Socket clientSocket = serverSocket.accept();
                    final String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                    new Thread(new ClientHandler(clientSocket), "ClientHandler-" + clientInfo).start();
                } catch (IOException e) {
                    UtilsServer.writeServerLog("Error al aceptar conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            UtilsServer.writeServerLog("[ERROR] No se ha podido levantar el servidor TCP/IP: " + e.getMessage());
        } finally {
            InactivityMonitor.stop();
        }
    }
}