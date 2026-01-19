package org.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La clase {@link Servidor} es la que se encarga de las conexiones TCP con los clientes. Este genera un hilo por cada
 * conexión. El objetivo es recibir o enviar el JSON pertinente al cliente. Por ejemplo, queremos recibir un JSON con los
 * datos de los agentes, recibiremos 1 JSON con los datos del agente. Nunca se enviarán más de 1 JSON.
 */
public class Servidor {

    private static final int PORT = UtilsServer.getServerPort("server.properties");
    private static final String HOST = UtilsServer.getServerName("server.properties");

    /**
     * La función el mensaje que ha enviado el cliente conectado, si es que envía algún mensaje.
     * Esto sirve para no obsfuscar el código
     * @param clientSocket Es el {@link Socket} del cliente, sirve para obtener el {@link java.io.InputStream}.
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

    public static void main(String[] args) {
        System.out.printf("[LOG] Servidor iniciado en %s:%d\n", HOST, PORT);

        try (final var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    final String json = readClientMsg(clientSocket);
                    System.out.println("[LOG]: " + json);
                    try {
                        clientSocket.close();
                        UtilsServer.writeServerLog("[LOG] Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                    } catch (IOException e) {
                        System.err.println("[ERROR] No se ha podido cerrar el socket del cliente.\n" + e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido crear un Socket para el server.\n" + e.getMessage());
        }
    }
}
