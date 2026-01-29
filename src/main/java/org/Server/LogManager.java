package org.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    private static final String LOG_FILE = "ironshield_system.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Niveles de log para categorizar la importancia
    public enum LogLevel {
        INFO,    // Conexiones, envíos exitosos
        WARNING, // Problemas de configuración menores
        ERROR,   // Fallos de socket o serialización
        CRITICAL // El servidor no puede arrancar o escribir archivos
    }

    /**
     * Método principal de escritura.
     * @param level Nivel de severidad
     * @param message Descripción del evento
     * @param e Excepción (opcional, puede ser null)
     */
    public static void log(LogLevel level, String message, Exception e) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String exceptionInfo = (e != null) ? " | Exception: " + e.getMessage() : "";
            
            // Formato profesional: [FECHA] [NIVEL] Mensaje
            String logEntry = String.format("[%s] [%s] %s%s%n", 
                                timestamp, level.name(), message, exceptionInfo);
            
            writer.write(logEntry);
            writer.flush();
            
            // Mostrar por consola según severidad
            if (level == LogLevel.ERROR || level == LogLevel.CRITICAL) {
                System.err.print("[LOGGED] " + logEntry);
            } else {
                System.out.print("[LOGGED] " + logEntry);
            }
        } catch (IOException ioException) {
            System.err.printf("[CRITICAL] Fallo total del sistema de logs: %s%n", ioException.getMessage());
        }
    }

    // Sobrecarga para mensajes rápidos sin excepción
    public static void info(String msg) { log(LogLevel.INFO, msg, null); }
    public static void warn(String msg) { log(LogLevel.WARNING, msg, null); }
    public static void error(String msg, Exception e) { log(LogLevel.ERROR, msg, e); }
    public static void error(String msg) { log(LogLevel.ERROR, msg, null); }
}