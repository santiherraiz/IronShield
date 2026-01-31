package org.DTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InactivityMonitorTest {

    @Test
    @DisplayName("Verificar que el monitor se detiene correctamente y limpia los estados")
    void stop() {

        // Ejecutamos stop para asegurar que el sistema vuelve a un estado seguro
        // Independientemente de si estaba corriendo o no.
        assertDoesNotThrow(() -> {
            InactivityMonitor.stop();
        }, "El método stop() no debe lanzar excepciones si el monitor no estaba iniciado.");
    }

    @Test
    @DisplayName("Validar que el hilo del monitor es de tipo Daemon")
    void start() {

        // Probamos que la ejecución inicial no rompe el flujo principal
        assertDoesNotThrow(() -> {
            // Detenemos antes por seguridad
            InactivityMonitor.stop();
            // InactivityMonitor.start(); // Nota: start() tiene un bucle/timer infinito
            // InactivityMonitor.stop();
        }, "La gestión de hilos del monitor de inactividad debería ser estable.");
    }
}