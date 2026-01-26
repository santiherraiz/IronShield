package org.Server;

import com.google.gson.Gson;
import org.DTO.Agent;
import org.DTO.ClientHandler;

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

    public static void main(String[] args) {
        System.out.printf("[LOG] Servidor iniciado en %s:%d\n", HOST, PORT);

        try (final var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido crear un Socket para el server.\n" + e.getMessage());
        }
    }
}
