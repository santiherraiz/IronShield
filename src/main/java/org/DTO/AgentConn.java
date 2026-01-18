package org.DTO;

import com.google.gson.Gson;
import org.Server.UtilsServer;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * La clase {@link AgentConn} es una clase que implementa la interfaz {@link Runnable} la cual sirve para manejar
 * la conexión al servidor TCP y envíar los datos del agente serializado a JSON. Aquí es donde se serializa a JSON.
 * <br>Esta clase tiene como atributos:
 * <br> - <strong>User</strong> es una instancia de {@link Agent} y es lo que serilalizará más adelante.
 */
public class AgentConn implements Runnable {
    /**
     * La función {@code run} viene de la interfaz {@link Runnable} y se ejecuta cuando empieza un hilo. La función
     * se conecta al servidor mediante un {@link Socket}, obteniendo el puerto y el nombre del host de la clase
     * {@link UtilsServer}. Después escribe el usuario serializado al {@link Socket} y el servidor lo recibe.
     */
    public void run() {
        try (
            final Socket socket = new Socket(
                    UtilsServer.getServerName("server.properties"),
                    UtilsServer.getServerPort("server.properties")
            );
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {

        } catch (Exception e) {
            System.err.println("[ERROR] No se ha podido enviar datos al servidor.\n" + e.getMessage());
        }
    }
}