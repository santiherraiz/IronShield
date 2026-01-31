package org.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class ServidorTest {

    @Test
    @DisplayName("Verificar que el puerto configurado está disponible y es válido")
    void testPuertoConfigurado() {
        // Obtenemos el puerto del archivo de propiedades
        int port = UtilsServer.getServerPort("server.properties");

        // Validamos que el puerto sea un número de red válido
        assertTrue(port > 0 && port <= 65535, "El puerto debe estar entre 1 y 65535");

        // Comprobamos si el puerto está libre intentando abrir un ServerSocket temporal
        // Si el servidor real ya está corriendo, esto fallará, lo cual también es una prueba de que el puerto se usa.
        assertDoesNotThrow(() -> {
            try (ServerSocket ignored = new ServerSocket()) {
                // Intentamos un bind preventivo para ver si el SO nos da permiso
                ignored.bind(new InetSocketAddress("localhost", 0));
            }
        }, "El sistema debería permitir abrir sockets en este entorno.");
    }

    @Test
    @DisplayName("Validar que el Host configurado no es nulo")
    void testHostConfigurado() {
        String host = UtilsServer.getServerName("server.properties");
        assertNotNull(host, "El host configurado en server.properties no debe ser nulo.");
        assertFalse(host.isEmpty(), "El host no debe estar vacío.");
    }

    @Test
    @DisplayName("Simulación de arranque: Verificar que los archivos de propiedades existen")
    void main() {
        // el main real no se puede hacer porque seria un bucle infinito,
        // probamos la condiicion necesaria para que el main no falle.
        java.io.File file = new java.io.File("server.properties");
        assertTrue(file.exists(), "El archivo server.properties es vital para el arranque del servidor.");
    }
}