package org.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.DAO.NombreDAO;
import org.DTO.Agent;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WebServer {
    // Puertos y nombres de host para el servidor y la conexión TCP
    private static final int PORT_WEBSERVER = UtilsServer.getServerPort("webserver.properties");
    private static final String HOST_WEBSERVER = UtilsServer.getServerName("webserver.properties");
    private static final int PORT_SERVER = UtilsServer.getServerPort("server.properties");
    private static final String HOST_SERVER = UtilsServer.getServerName("server.properties");

    private static Agent deserialise(String body) {
        return new Gson().fromJson(body, Agent.class);
    }

    private static String serialise(String res) {
        return new Gson().toJson(res);
    }

    /**
     * Esta función comprueba que se envía al servidor TCP. Hace un socket y lo conecta al servidor, después escribe
     * el body proporcionado por el {@link HttpExchange} para que el servidor lo reciba.
     * @param body Mensaje enviado desde la aplicación
     * @return True o false si no hay un error o lo hay respectivamente.
     */
    private static boolean sendServer(String body) {
        try (
                Socket socket = new Socket(HOST_SERVER, PORT_SERVER);
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

    /**
     * Esta función añade las cabeceras CORS para que, si en un emulador web, de acceso el navegador a la petición.
     * Después mediante {@link HttpExchange} obtiene las cabeceras de la respuesta, la construye en un {@link String}
     * obteniendo el cuerpo de la respuesta, leyendo byte a byte y estandarizándolo en UTF-8. Después hace una respuesta
     * para el solicitante y se la envía, cerrando el exchange.
     * @param exchange {@link HttpExchange} encapsula una petición y una respuesta HTTP. Puede examinar la solicitud y
     *                                     construir y enviar una respuesta.
     */
    private static void handleLocation(HttpExchange exchange) {
        try {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("[LOG] Localización: " + body);
            String response = "Enviado";
            if (!sendServer(body)) {
                response = "No se ha podido enviar al Servidor TCP";
            }
            exchange.sendResponseHeaders(200, response.length());
            exchange.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error al recibir la localización");
        }
    }

    private static void handleName(HttpExchange exchange) {
        try {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            final Agent agent = deserialise(body);
            final String name = NombreDAO.obtenerNombrePorUsuario(agent.username, agent.pass);
            System.out.println(name);
            String jsonResponse = serialise(name);
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, responseBytes.length);
            final OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
            exchange.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error al recibir la localización");
        }
    }


    public static void main(String[] args) {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOST_WEBSERVER, PORT_WEBSERVER), 0);
            server.createContext("/location", WebServer::handleLocation);
            server.createContext("/name", WebServer::handleName);
            server.setExecutor(null);
            server.start();
            System.out.println("[INFO] WebServer escuchando en " + HOST_WEBSERVER + ":" + PORT_WEBSERVER);
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error en el servidor.\n" + e.getMessage());
        }

    }
}
