package org.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class LogManagerTest {

    @Test
    @DisplayName("Verificar que se crea el archivo de log y se puede escribir un mensaje de INFO")
    void info() {
        // Enviamos un mensaje de información
        LogManager.info("Test de unidad: mensaje de información");

        //Verificamos que el archivo físico existe
        File logFile = new File("ironshield_system.log");
        assertTrue(logFile.exists(), "El archivo de log debería haberse creado.");
    }

    @Test
    @DisplayName("Validar el registro de advertencias (WARNING)")
    void warn() {
        // Comprobamos que no explota al escribir una advertencia
        assertDoesNotThrow(() -> {
            LogManager.warn("Test de unidad: advertencia de seguridad");
        }, "El método warn() no debería lanzar ninguna excepción.");
    }

    @Test
    @DisplayName("Probar el registro de errores con una excepción simulada")
    void error() {
        // 1. Creamos una excepción ficticia
        Exception e = new Exception("Error simulado para el test");

        // 2. Verificamos que el log maneja la excepción sin romperse
        assertDoesNotThrow(() -> {
            LogManager.error("Test de unidad: fallo de servidor", e);
        }, "El sistema de logs debería capturar el error de escritura si lo hubiera.");
    }

    @Test
    @DisplayName("Verificar el método principal de log con nivel CRITICAL")
    void log() {
        // Probamos el método base con el nivel más alto de severidad
        assertDoesNotThrow(() -> {
            LogManager.log(LogManager.LogLevel.CRITICAL, "Fallo crítico de prueba", null);
        });
    }

    @Test
    @DisplayName("Validar sobrecarga del método error sin objeto Exception")
    void testError() {
        assertDoesNotThrow(() -> {
            LogManager.error("Mensaje de error simple sin excepción");
        });
    }
}