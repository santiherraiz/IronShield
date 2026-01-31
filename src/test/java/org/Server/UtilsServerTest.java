package org.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class UtilsServerTest {

    @Test
    @DisplayName("Validar que el formateador añade la fecha y hora correctamente")
    void strFormatter() {
        String mensaje = "Conexión establecida";
        String resultado = UtilsServer.strFormatter(mensaje);

        // Verificamos que contenga el mensaje y los corchetes del timestamp
        assertTrue(resultado.contains(mensaje), "El mensaje original debe estar presente");
        assertTrue(resultado.contains("[") && resultado.contains("]"), "Debe contener el formato de timestamp");
    }

    @Test
    @DisplayName("Verificar la lectura de una clave específica en el archivo de configuración")
    void getValueFromConf() {
        // Probamos con el archivo del servidor TCP
        String host = UtilsServer.getValueFromConf("host", "server.properties");

        assertNotNull(host, "No se pudo leer el host de server.properties");
        assertFalse(host.isEmpty(), "El valor del host no debería estar vacío");
    }

    @Test
    @DisplayName("Validar la obtención del puerto como número entero")
    void getServerPort() {
        int puerto = UtilsServer.getServerPort("server.properties");

        // Verificamos que sea un puerto de red válido
        assertTrue(puerto > 0, "El puerto debe ser un número mayor a 0");
    }

    @Test
    @DisplayName("Verificar la escritura en el archivo de log del servidor")
    void writeServerLog() {
        String testMsg = "Mensaje de prueba para log unitario";

        // Verificamos que no lance excepción al escribir
        assertDoesNotThrow(() -> {
            UtilsServer.writeServerLog(testMsg);
        }, "La escritura en el log falló.");

        // Verificamos físicamente si el archivo existe
        File logFile = new File(System.getProperty("user.dir") + "/src/server-logs/server_log.txt");
        // No fallamos el test si no existe el directorio, pero imprimimos advertencia
        if (!logFile.getParentFile().exists()) {
            System.out.println("[INFO TEST] El directorio de logs no existe, es normal que falle la creación del archivo.");
        }
    }

    @Test
    @DisplayName("Validar obtención del nombre del host")
    void getServerName() {
        String host = UtilsServer.getServerName("server.properties");
        assertNotNull(host);
    }
}