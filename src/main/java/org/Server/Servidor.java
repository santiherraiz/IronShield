package org.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.DTO.User;
import org.DTO.UserConn;

public class Servidor {

    private static final int PORT = UtilsServer.getServerPort();
    private static final String HOST = UtilsServer.getServerName();

    /**
     * La función el mensaje que ha enviado el cliente conectado, si es que envía algún mensaje.
     * Esto sirve para no obsfuscar el código
     * @param clientSocket Es el {@link Socket} del cliente, sirve para obtener el {@link java.io.InputStream}.
     * @return Devuelve el mensaje que ha leído.
     */
    private static String readClientMsg(Socket clientSocket) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            return br.readLine();
        } catch (Exception e) {
            UtilsServer.writeServerLog("[ERROR] No se ha pidod leer el mensaje");
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.printf("[LOG] Servidor iniciado en %s:%d\n", HOST, PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.printf("Cliente conectado desde %s:%d\n", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
                final String json = readClientMsg(clientSocket);
                System.out.println("[LOG]: " + json);
                if (clientSocket.isClosed()) {
                    UtilsServer.writeServerLog("[LOG] Cliente con IP " + clientSocket.getInetAddress().getHostAddress() + " se ha desconectado");
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido crear un Socket para el server.\n" + e.getMessage());
        }
    }
}
