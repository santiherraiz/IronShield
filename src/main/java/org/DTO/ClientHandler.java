package org.DTO;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.DAO.GuardiaDAO;
import org.Server.UtilsServer;

import java.io.*;
import java.net.Socket;

public record ClientHandler(Socket clientSocket) implements Runnable {
    private static final int PORT_WEBSERVER = UtilsServer.getServerPort("webserver.properties");
    private static final String HOST_WEBSERVER = UtilsServer.getServerName("webserver.properties");
    /**
     * La función el mensaje que ha enviado el cliente conectado, si es que envía algún mensaje.
     * Esto sirve para no ofuscar el código
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

    /**
     * Esta función comprueba que se envía al servidor TCP. Hace un socket y lo conecta al servidor, después escribe
     * el body proporcionado por el {@link HttpExchange} para que el servidor lo reciba.
     * @param body Mensaje enviado desde la aplicación
     * @return True o false si no hay un error o lo hay respectivamente.
     */
    private static boolean sendWebServer(String body) {
        try (
                Socket socket = new Socket(HOST_WEBSERVER, PORT_WEBSERVER);
                final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            bw.write(body);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private static Agent deserialise(String body) {
        return new Gson().fromJson(body, Agent.class);
    }

    private static String serialise(String res) {
        return new Gson().toJson(res);
    }

    @Override
    public void run() {
        final String json = readClientMsg(clientSocket);
        System.out.println("[LOG]: " + json);
//        final Agent agent = deserialise(json);
//        GuardiaDAO.insertarPosicion(agent.username, agent.latitude, agent.longitude);
        try {
            clientSocket.close();
            UtilsServer.writeServerLog("[LOG] Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido cerrar el socket del cliente.\n" + e.getMessage());
        }
    }
}
