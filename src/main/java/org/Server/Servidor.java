package org.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.net.httpserver.HttpServer;
import org.DTO.ClienteConex;

public class Servidor {

    private static final int PORT = UtilsServer.getServerPort();
    private static final String HOST = UtilsServer.getServerName();
    private static int contadorAgentes = 0;

    public static void main(String[] args) {
        System.out.printf("[LOG] Servidor iniciado en %s:%d\n", HOST, PORT);
        try {
            final HttpServer httpServer = HttpServer.create(new InetSocketAddress(HOST, PORT == 80 ? 8080 : PORT + 1), 0);
            httpServer.createContext("/log-error", new LogController());
            httpServer.setExecutor(null);
            httpServer.start();
            System.out.println("[LOG] LogController escuchando en /log-error");
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido crear el Servidor HTTP: " + e.getMessage());
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                contadorAgentes++;
                final Socket clientSocket = serverSocket.accept();
                System.out.printf("Cliente conectado desde %s:%d\n", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
                final ClienteConex clientCNX = new ClienteConex(clientSocket, contadorAgentes);
                new Thread(clientCNX).start();

            }
        } catch (IOException e) {
            System.err.printf("[ERROR] No se ha podido crear un Socket para el server: %s\n", e.getMessage());
        }
    }
}
