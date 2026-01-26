package org.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.DAO.ConsultaDAO;
import org.DAO.GuardiaDAO;
import org.DAO.NombreDAO;
import org.DTO.Agent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WebServer {
    // Puertos y nombres de host para el servidor y la conexión TCP
    private static final int PORT_WEBSERVER = UtilsServer.getServerPort("webserver.properties");
    private static final String HOST_WEBSERVER = UtilsServer.getServerName("webserver.properties");
    private static final int PORT_SERVER = UtilsServer.getServerPort("server.properties");
    private static final String HOST_SERVER = UtilsServer.getServerName("server.properties");

    /**
     * Esta función comprueba que se envía al servidor TCP. Hace un socket y lo conecta al servidor, después escribe
     * el body proporcionado por el {@link HttpExchange} para que el servidor lo reciba.
     * @param body Mensaje enviado desde la aplicación
     * @return True o false si no hay un error o lo hay respectivamente.
     */
    private static String sendServer(String body) {
        try (
                Socket socket = new Socket(HOST_SERVER, PORT_SERVER);
                final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Envío al TCP
            bw.write(body);
            bw.newLine();
            bw.flush();

            // Aquí devuelve la respuesta
            return br.readLine();
        } catch (Exception e) {
            return null;
        }
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
            sendServer(body);
            exchange.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error al envíar la localización");
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
            String tcpResponse = sendServer(body);

            if (tcpResponse == null) tcpResponse = "Error TCP";

            exchange.sendResponseHeaders(200, tcpResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(tcpResponse.getBytes());
            os.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error al mandar la localización");
        }
    }

    private static void handleAlertLog(HttpExchange exchange) {
        try {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            List<Agent> alerts = ConsultaDAO.obtenerAlertas();
            String json = new Gson().toJson(alerts);
            exchange.sendResponseHeaders(200, json.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error al recibir los registros de alertas.");
        }
    }


    public static void main(String[] args) {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOST_WEBSERVER, PORT_WEBSERVER), 0);
            server.createContext("/location", WebServer::handleLocation);
            server.createContext("/name", WebServer::handleName);
            server.createContext("/alert-log", WebServer::handleAlertLog);
            server.setExecutor(null);
            server.start();
            System.out.println("[INFO] WebServer escuchando en " + HOST_WEBSERVER + ":" + PORT_WEBSERVER);
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error en el servidor.\n" + e.getMessage());
        }

    }
}
