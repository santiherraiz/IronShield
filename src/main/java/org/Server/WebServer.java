package org.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebServer {
    private static final int PORT = UtilsServer.getServerPort("webserver.properties");
    private static final String HOST = UtilsServer.getServerName("webserver.properties");

    static void main() {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
            server.createContext("/", (HttpExchange exchange) -> {
                final String response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                try (final OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    System.err.println("[ERROR] No se pudieron recibir datos.");
                }
            });
            server.setExecutor(null);
            server.start();
            System.out.println("[INFO] WebServer escuchando en " + HOST + ":" + PORT);
        } catch (Exception e) {
            System.err.println("[ERROR] Ha habido un error en el servidor.\n" + e.getMessage());
        }

    }
}
