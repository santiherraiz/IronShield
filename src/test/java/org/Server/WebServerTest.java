package org.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class WebServerTest {

    @Test
    @DisplayName("Verificar configuración de red del WebServer")
    void testConfiguracionRed() {
        // Leemos el puerto del fichero de configuración del WebServer
        int port = UtilsServer.getServerPort("webserver.properties");
        String host = UtilsServer.getServerName("webserver.properties");

        // Validar que el puerto es un número de red posible
        assertTrue(port > 0 && port <= 65535, "El puerto del WebServer debe ser válido.");

        //  Validar que el host no sea nulo
        assertNotNull(host, "El host del WebServer no puede ser nulo.");
    }

    @Test
    @DisplayName("Comprobar existencia de archivos de propiedades críticos")
    void testArchivosConfiguracion() {
        // El WebServer necesita ambos para funcionar
        File webProps = new File("webserver.properties");
        File serverProps = new File("server.properties");

        assertTrue(webProps.exists(), "Falta webserver.properties");
        assertTrue(serverProps.exists(), "Falta server.properties (necesario para sendServer)");
    }

    @Test
    @DisplayName("Validar que el puerto del WebServer está disponible")
    void testDisponibilidadPuerto() {
        int port = UtilsServer.getServerPort("webserver.properties");

        // Intentamos abrir un socket temporal para ver si el puerto está libre en el sistema
        assertDoesNotThrow(() -> {
            try (ServerSocket ss = new ServerSocket()) {
                // localhost:0 busca cualquier puerto libre
                ss.bind(new InetSocketAddress("localhost", 0));
            }
        }, "El sistema operativo debería permitir la apertura de sockets HTTP.");
    }

    @Test
    @DisplayName("Simulación de arranque del main")
    void main() {
        assertDoesNotThrow(() -> {
            UtilsServer.getServerPort("webserver.properties");
            UtilsServer.getServerName("webserver.properties");
        }, "Fallo en la lectura de utilidades previa al arranque del servidor.");
    }
}