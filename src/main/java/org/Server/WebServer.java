package org.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.DAO.ConsultaDAO;
import org.DAO.GuardiaDAO;
import org.DTO.Agent;
import org.DTO.InactivityMonitor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * WebServer HTTP que actúa como intermediario entre la aplicación móvil y el servidor TCP.
 * Maneja peticiones REST y las redirige al servidor TCP cuando es necesario.
 * 
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

    /**
     * Añade headers CORS a la respuesta HTTP.
     * Necesario para permitir peticiones desde navegadores web y emuladores.
     */
    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * Maneja peticiones OPTIONS para CORS preflight.
     */
    private static boolean handleCorsPreFlight(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        return false;
    }

    /**
     * Maneja la actualización de ubicación del guardia.
     * Recibe la posición y la envía al servidor TCP para su procesamiento.
     * 
     * @param exchange Encapsula la petición y respuesta HTTP
     */
    private static void handleLocation(HttpExchange exchange) {
        try {
            if (handleCorsPreFlight(exchange)) return;

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            addCorsHeaders(exchange);

            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            LogManager.info("[LOCATION] Recibida actualización: " + body);
            
            sendServer(body);
            
            // Enviar respuesta de éxito al cliente
            String response = "{\"status\":\"ok\",\"message\":\"Ubicación actualizada\"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
            
        } catch (Exception e) {
            LogManager.error("Error al manejar ubicación: " + e.getMessage());
            try {
                String errorResponse = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
                exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
                exchange.getResponseBody().write(errorResponse.getBytes());
                exchange.close();
            } catch (IOException ioEx) {
                LogManager.error("Error al enviar respuesta de error: " + ioEx.getMessage());
            }
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
            if (handleCorsPreFlight(exchange)) return;

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            addCorsHeaders(exchange);

            final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            LogManager.info("[NAME] Petición de nombre recibida");
            
            String tcpResponse = sendServer(body);

            if (tcpResponse == null) {
                tcpResponse = "\"Error de comunicación con servidor\"";
            }

            exchange.sendResponseHeaders(200, tcpResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(tcpResponse.getBytes());
            os.close();
            
        } catch (Exception e) {
            LogManager.error("Error al manejar nombre: " + e.getMessage());
            try {
                exchange.sendResponseHeaders(500, -1);
                exchange.close();
            } catch (IOException ioEx) {
                LogManager.error("Error al enviar respuesta de error: " + ioEx.getMessage());
            }
        }
    }

    /**
     * Maneja la petición del log de alertas.
     * Obtiene el historial de alertas directamente de la base de datos.
     * 
     * @param exchange Encapsula la petición y respuesta HTTP
     */
    private static void handleAlertLog(HttpExchange exchange) {
        try {
            if (handleCorsPreFlight(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            addCorsHeaders(exchange);
            
            final List<Agent> alerts = ConsultaDAO.obtenerAlertas();
            final String json = new Gson().toJson(alerts);
            
            LogManager.info(String.format("[ALERT-LOG] Devolviendo %d alertas", alerts.size()));
            
            exchange.sendResponseHeaders(200, json.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
            
        } catch (Exception e) {
            LogManager.error("Error al obtener alertas: " + e.getMessage());
            try {
                String errorResponse = "{\"error\":\"" + e.getMessage() + "\"}";
                exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
                exchange.getResponseBody().write(errorResponse.getBytes());
                exchange.close();
            } catch (IOException ioEx) {
                LogManager.error("Error al enviar respuesta de error: " + ioEx.getMessage());
            }
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
            if (handleCorsPreFlight(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            addCorsHeaders(exchange);
            
            // IMPORTANTE: obtenerTodos() ahora devuelve SOLO guardias activos
            // El filtrado se hace en el backend con SQL, no en el frontend
            final List<Agent> guards = GuardiaDAO.obtenerTodos();
            final String json = new Gson().toJson(guards);
            
            LogManager.info(String.format("[GUARDS] Devolviendo %d guardias activos", guards.size()));
            
            exchange.sendResponseHeaders(200, json.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
            
        } catch (Exception e) {
            LogManager.error("Error al obtener guardias: " + e.getMessage());
            e.printStackTrace();
            try {
                String errorResponse = "[]"; // Devolver array vacío en caso de error
                exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
                exchange.getResponseBody().write(errorResponse.getBytes());
                exchange.close();
            } catch (IOException ioEx) {
                LogManager.error("Error al enviar respuesta de error: " + ioEx.getMessage());
            }
        }
    }

    /**
     * NUEVO: Endpoint para obtener guardias inactivos.
     * Útil para el supervisor para ver alertas específicas.
     */
    private static void handleInactiveGuards(HttpExchange exchange) {
        try {
            if (handleCorsPreFlight(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            addCorsHeaders(exchange);
            
            final List<Agent> inactiveGuards = GuardiaDAO.obtenerInactivos();
            final String json = new Gson().toJson(inactiveGuards);
            
            LogManager.info(String.format("[INACTIVE] Devolviendo %d guardias inactivos", inactiveGuards.size()));
            
            exchange.sendResponseHeaders(200, json.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
            
        } catch (Exception e) {
            LogManager.error("Error al obtener guardias inactivos: " + e.getMessage());
            try {
                String errorResponse = "[]";
                exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
                exchange.getResponseBody().write(errorResponse.getBytes());
                exchange.close();
            } catch (IOException ioEx) {
                LogManager.error("Error al enviar respuesta de error: " + ioEx.getMessage());
            }
        }
    }

    /**
     * NUEVO: Endpoint de salud del servidor.
     * Útil para monitorear que el servidor está funcionando.
     */
    private static void handleHealth(HttpExchange exchange) {
        try {
            if (handleCorsPreFlight(exchange)) return;

            addCorsHeaders(exchange);
            
            String health = String.format(
                "{\"status\":\"ok\",\"monitor\":\"%s\",\"timestamp\":%d}",
                InactivityMonitor.isRunning() ? "running" : "stopped",
                System.currentTimeMillis()
            );
            
            exchange.sendResponseHeaders(200, health.getBytes().length);
            exchange.getResponseBody().write(health.getBytes());
            exchange.close();
            
        } catch (Exception e) {
            LogManager.error("Error en health check: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOST_WEBSERVER, PORT_WEBSERVER), 0);
            
            // Endpoints existentes
            server.createContext("/location", WebServer::handleLocation);
            server.createContext("/name", WebServer::handleName);
            server.createContext("/alert-log", WebServer::handleAlertLog);
            server.createContext("/guards", WebServer::handleGuards);
            
            // Nuevos endpoints
            server.createContext("/inactive-guards", WebServer::handleInactiveGuards);
            server.createContext("/health", WebServer::handleHealth);
            
            server.setExecutor(null);
            server.start();
            
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║         TRACKMATE - WEB SERVER           ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println();
            LogManager.info(String.format("WebServer escuchando en %s:%d", HOST_WEBSERVER, PORT_WEBSERVER));
            System.out.println();
            System.out.println("Endpoints disponibles:");
            System.out.println("  POST   /location         - Actualizar ubicación del guardia");
            System.out.println("  POST   /name             - Obtener nombre de usuario");
            System.out.println("  GET    /guards           - Obtener guardias ACTIVOS");
            System.out.println("  GET    /inactive-guards  - Obtener guardias INACTIVOS");
            System.out.println("  GET    /alert-log        - Obtener log de alertas");
            System.out.println("  GET    /health           - Estado del servidor");
            System.out.println();
            System.out.println("✓ Servidor listo. Presiona Ctrl+C para detener.");
            
        } catch (Exception e) {
            LogManager.error("ERROR CRÍTICO en WebServer: " + e.getMessage());
            e.printStackTrace();
            System.err.println();
            System.err.println("El WebServer no pudo iniciarse. Verifica:");
            System.err.println("   1. Que el puerto " + PORT_WEBSERVER + " no esté en uso");
            System.err.println("   2. Que tengas permisos para usar ese puerto");
            System.err.println("   3. Que el archivo webserver.properties esté configurado correctamente");
            System.exit(1);
        }
    }
}