package org.Server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Servidor {

    private static final int PORT = UtilsServer.getServerPort();
    private static final String HOST = UtilsServer.getServerName();

    static void main(String[] args) {
        System.out.printf("[LOG] Servidor iniciado en %s:%d\n", HOST, PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("Cliente conectado desde %s:%d\n", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
            }
        } catch (IOException e) {
            System.err.printf("[ERROR] No se ha podido crear un Socket para el server: %s\n", e.getMessage());
        }
    }   
}
