package org.DTO;

import org.Server.UtilsServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public record ClientHandler(Socket clientSocket) implements Runnable {
    /**
     * La función el mensaje que ha enviado el cliente conectado, si es que envía algún mensaje.
     * Esto sirve para no obsfuscar el código
     *
     * @param clientSocket Es el {@link Socket} del cliente, sirve para obtener el {@link InputStream}.
     * @return Devuelve el mensaje que ha leído.
     */
    private static String readClientMsg(Socket clientSocket) {
        try {
            final var br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return br.readLine();
        } catch (Exception e) {
            UtilsServer.writeServerLog("[ERROR] No se ha podido leer el mensaje");
        }

        return null;
    }

    @Override
    public void run() {
        final String json = readClientMsg(clientSocket);
        System.out.println("[LOG]: " + json);
        try {
            clientSocket.close();
            UtilsServer.writeServerLog("[LOG] Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido cerrar el socket del cliente.\n" + e.getMessage());
        }
    }
}
