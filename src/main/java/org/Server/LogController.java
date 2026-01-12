package org.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Recibe errores de React y crea archivos XML
 */
public class LogController implements HttpHandler {

    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try (InputStream is = exchange.getRequestBody();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // Parsear JSON simple (sin librerías)
            String json = sb.toString();
            String error = extraerValor(json, "error");
            String component = extraerValor(json, "component");
            String timestamp = extraerValor(json, "timestamp");

            // Crear XML
            crearXmlError(error, component, timestamp);

            // Responder
            String response = "{\"status\": \"ok\"}";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void crearXmlError(String error, String component, String timestamp) {
        try {
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }

            String fileName = "logs/error_" + System.currentTimeMillis() + ".xml";
            
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<Error>\n");
            xml.append("  <Timestamp>").append(timestamp).append("</Timestamp>\n");
            xml.append("  <Component>").append(escapeXml(component)).append("</Component>\n");
            xml.append("  <Message>").append(escapeXml(error)).append("</Message>\n");
            xml.append("</Error>\n");

            try (FileWriter fw = new FileWriter(fileName)) {
                fw.write(xml.toString());
            }

            System.out.println("[✓] XML creado: " + fileName);

        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo crear XML: " + e.getMessage());
        }
    }

    private String extraerValor(String json, String clave) {
        String buscar = "\"" + clave + "\":\"";
        int inicio = json.indexOf(buscar);
        if (inicio == -1) return "";
        
        inicio += buscar.length();
        int fin = json.indexOf("\"", inicio);
        if (fin == -1) return "";
        
        return json.substring(inicio, fin);
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}

