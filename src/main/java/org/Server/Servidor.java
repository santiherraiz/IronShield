package org.Server;

import org.Config.AppConfig;
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
 * 
 * NUEVO: Ahora también inicia el monitor de inactividad para detectar guardias inactivos automáticamente.
 */
public class Servidor {
    private static final int PORT = UtilsServer.getServerPort("server.properties");
    private static final String HOST = UtilsServer.getServerName("server.properties");

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         TRACKMATE - IRONSHIELD SERVER        ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
        
        // Mostrar configuración
        System.out.println(AppConfig.getConfigInfo());
        System.out.println();
        
        LogManager.info(String.format("Servidor iniciado en %s:%d", HOST, PORT));
        
        // Iniciar el monitor de inactividad
        try {
            InactivityMonitor.start();
        } catch (Exception e) {
            LogManager.error("Error al iniciar el monitor de inactividad: " + e.getMessage());
            e.printStackTrace();
            System.err.println("ADVERTENCIA: El monitor de inactividad no se pudo iniciar.");
            System.err.println("El servidor continuará funcionando pero sin detección automática.");
        }
        
        // Agregar shutdown hook para detener el monitor limpiamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LogManager.info("Señal de apagado recibida. Deteniendo servicios...");
            InactivityMonitor.stop();
            LogManager.info("Servidor detenido correctamente.");
        }));
        
        // Iniciar el servidor TCP
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            LogManager.info("Servidor TCP listo para aceptar conexiones");
            System.out.println("Servidor listo. Presiona Ctrl+C para detener.");
            System.out.println();
            
            while (true) {
                try {
                    final Socket clientSocket = serverSocket.accept();
                    
                    // Log de nueva conexión
                    String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                    LogManager.info("Nueva conexión entrante desde: " + clientInfo);
                    
                    // Crear un hilo para manejar el cliente
                    Thread clientThread = new Thread(new ClientHandler(clientSocket));
                    clientThread.setName("ClientHandler-" + clientInfo);
                    clientThread.start();
                    
                } catch (IOException e) {
                    LogManager.error("Error al aceptar conexión del cliente: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            LogManager.error("ERROR CRÍTICO: No se ha podido crear el ServerSocket");
            LogManager.error("Detalles: " + e.getMessage());
            e.printStackTrace();
            System.err.println();
            System.err.println("El servidor no pudo iniciarse. Verifica:");
            System.err.println("   1. Que el puerto " + PORT + " no esté en uso");
            System.err.println("   2. Que tengas permisos para usar ese puerto");
            System.err.println("   3. Que el archivo server.properties esté configurado correctamente");
            System.exit(1);
        } finally {
            // Asegurar que el monitor se detenga al salir
            InactivityMonitor.stop();
        }
    }
}