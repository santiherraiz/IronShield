package org.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    private static final String LOG_FILE = "server_error.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void logError(String errorMessage, Exception exception) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s - %s%n", timestamp, errorMessage, exception.getMessage());
            writer.write(logEntry);
            writer.flush();
            System.err.printf("[ERROR LOGGED] %s%n", logEntry);
        } catch (IOException e) {
            System.err.printf("[CRITICAL] No se pudo escribir en el log: %s%n", e.getMessage());
        }
    }

    public static void logError(String errorMessage) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s%n", timestamp, errorMessage);
            writer.write(logEntry);
            writer.flush();
            System.err.printf("[ERROR LOGGED] %s%n", logEntry);
        } catch (IOException e) {
            System.err.printf("[CRITICAL] No se pudo escribir en el log: %s%n", e.getMessage());
        }
    }
}
