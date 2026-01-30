package org.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.DAO.GuardiaDAO;
import org.DTO.Agent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * WebServer HTTP que actúa como intermediario entre la aplicación móvil y el servidor TCP.
 * Maneja peticiones REST y las redirige al servidor TCP cuando es necesario.
 * <p>
 * MEJORADO: Ahora el endpoint /guards devuelve solo guardias activos (filtrados en backend).
 */
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
     * @return Respuesta del servidor TCP, o null si hay error
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
            LogManager.error("Error al comunicar con servidor TCP: " + e.getMessage());
            return null;
        }
    }

    private static void handleLocation(HttpExchange exchange) {
        try {
            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            sendServer(body);
            // Enviar respuesta de éxito al cliente
            String response = "{\"status\":\"ok\",\"message\":\"Ubicación actualizada\"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();

        } catch (Exception e) {
            LogManager.error("Error al manejar ubicación: " + e.getMessage());
        }
    }

    /**
     * Maneja la petición de nombre de usuario.
     * Envía las credenciales al servidor TCP y devuelve el nombre del usuario.
     * 
     * @param exchange Encapsula la petición y respuesta HTTP
     */
    private static void handleName(HttpExchange exchange) {
        try {
            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            final String tcpResponse = sendServer(body);
            assert tcpResponse != null;
            exchange.sendResponseHeaders(200, tcpResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(tcpResponse.getBytes());
            os.close();
        } catch (Exception e) {
            LogManager.error("Error al manejar nombre: " + e.getMessage());
        }
    }

    /**
     * Maneja la petición de lista de guardias.
     * MEJORADO: Ahora devuelve solo guardias ACTIVOS, filtrados en el backend.
     * 
     * @param exchange Encapsula la petición y respuesta HTTP
     */
    private static void handleGuards(HttpExchange exchange) {
        try {
            // IMPORTANTE: obtenerTodos() ahora devuelve SOLO guardias activos
            // El filtrado se hace en el backend con SQL, no en el frontend
            final List<Agent> guards = GuardiaDAO.obtenerTodos();
            final String json = new Gson().toJson(guards);
            exchange.sendResponseHeaders(200, json.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        } catch (Exception e) {
            LogManager.error("Error al obtener guardias: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOST_WEBSERVER, PORT_WEBSERVER), 0);
            server.createContext("/location", WebServer::handleLocation);
            server.createContext("/name", WebServer::handleName);
            server.createContext("/guards", WebServer::handleGuards);
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            LogManager.error("ERROR CRÍTICO en WebServer: " + e.getMessage());
        }
    }
}