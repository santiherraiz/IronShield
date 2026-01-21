package org.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

/**
 * La clase {@link UtilsServer} es una clase con funciones para el conjunto del paquete y proyecto que, en sí, afectan
 * de alguna manera al servidor. Aquí podemos encontrar la lógica para obtener el puerto y el host del fichero de
 * configuración del servidor. También tenemos un pequeño log del servidor, aquí toda la información, para no hacer
 * ruidosa la consola, se meterá aquí, tal como si un usuario se ha desconectado y si ha enviado un mensaje,
 * con fecha y hora. La función para escribir también está asociada a esta clase.
 */
public class UtilsServer {
    // Fichero de log
    private static final String SERVER_LOG = System.getProperty("user.dir") + "/src/server-logs/server_log.txt";

    /**
     * La función para escribir en el fichero log. Llama a una función que formatea el string para ponerle
     * la fecha y hora
     * @param msg El mensaje que se va a escribir
     */
    public static void writeServerLog(String msg) {
        final String log = LogManager.info(msg);
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(SERVER_LOG, true))) {
            bw.write(log);
            bw.newLine();
        } catch (Exception e) {
            System.err.println("[ERROR] No se puede escribir en el fichero");
        }
    }

    /**
     * La función es el formatter para añadir el tiempo y la hora al mismo.
     * @param msg El mensaje que se va a formatear
     * @return Devuelve el mensaje formateado
     */
    public static String strFormatter(String msg) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        return msg + "[" + timestamp + "]";
    }

    /**
     * La función básicamente coge el puerto y el host del fichero de configuración. Mete el resultado, después
     * de dividirlo por su divisor, en un {@link HashMap} el cual contiene clave, valor. Esto se hace,
     * a pesar de consumir bastantes recursos, para que el envío sea más ordenado.
     * @param key Es el campo que quieres buscar (debe estar dentro del fichero de configuración, si no dará null)
     * @return El valor de la clave pasada, si hay un problema o no es válida la clave, devuelve null.
     */
    private static String getValueFromConf(String key, String path) {
        try (final BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            final HashMap<String, String> dict = new HashMap<>();
            while ((line = br.readLine()) != null) {
                final String[] parts = line.split("=");
                dict.put(parts[0], parts[1]);
            }

            if (!dict.containsKey(key)) {
                System.out.println("[WARN] No hay un campo especificado en el fichero de configuración. Devolviendo NULL");
                return null;
            }

            key = dict.get(key);

        } catch (Exception e) {
            System.err.printf("[ERROR] No se pudo recoger el nombre del servidor: %s\n", e.getMessage());
        }

        return key;
    }

    /**
     * @return Devuelve el nombre del servidor llamando a la función {@code getValueFromConf}
     */
    public static String getServerName(String path) {
        return getValueFromConf("host", path);
    }

    /**
     * @return Devuelve el puerto del servidor llamando a la función {@code getValueFromConf},
     * comprobando si se puede parsear a {@link Integer}
     */
    public static int getServerPort(String path) {
        try {
            return Integer.parseInt(Objects.requireNonNull(getValueFromConf("port", path)));
        } catch (Exception e) {
            System.err.println("[ERROR] Hay un error al parsear el puerto a Integer (Es NULL, seguramente sea null)");
        }

        return 0;
    }
}