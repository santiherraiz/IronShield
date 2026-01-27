package org.DTO;

import com.google.gson.Gson;
import org.DAO.GuardiaDAO;
import org.DAO.NombreDAO;
import org.Server.UtilsServer;

import java.io.*;
import java.net.Socket;

public record ClientHandler(Socket clientSocket) implements Runnable {
    private static Agent deserialise(String body) { return new Gson().fromJson(body, Agent.class); }
    private static String serialise(String res) { return new Gson().toJson(res); }
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

    private static void handleName(final Agent agent, final Socket clientSocket) {
        final String name = NombreDAO.obtenerNombrePorUsuario(agent.username, agent.pass);
        final String jsonResponse = serialise(name);

        try {
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            bw.write(jsonResponse);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            System.err.println("[ERROR] Error al enviar respuesta al WebServer.");
        }
    }

    private static void handleLoc(final Agent agent) {
        GuardiaDAO.insertarPosicion(agent.username, agent.latitude, agent.longitude);
    }

    @Override
    public void run() {
        final String json = readClientMsg(clientSocket);
        System.out.println("[LOG]: " + json);
        final Agent agent = deserialise(json);
        switch (agent.code) {
            case 1:
                handleName(agent, clientSocket);
                break;
            case 3:
                handleLoc(agent);
                break;
            default:
                System.out.println("No implementado");
                return;
        }

        try {
            clientSocket.close();
            UtilsServer.writeServerLog("[LOG] Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        } catch (IOException e) {
            System.err.println("[ERROR] No se ha podido cerrar el socket del cliente.\n" + e.getMessage());
        }
    }
}
