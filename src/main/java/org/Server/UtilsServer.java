package org.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;

class UtilsServer {
    private static final String PATH = "server.properties";

    private static String getValueFromConf(String key) {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            final HashMap<String, String> dict = new HashMap<>();
            while ((line = br.readLine()) != null) {
                final String[] parts = line.split("=");
                dict.put(parts[0], parts[1]);
            }

            if (!dict.containsKey(key)) {
                System.out.println("[WARN] No hay un host especificado en el fichero de configuración. Devolviendo NULL");
                return null;
            }

            key = dict.get(key);

        } catch (Exception e) {
            System.err.printf("[ERROR] No se pudo recoger el nombre del servidor: %s\n", e.getMessage());
        }

        return key;
    }

    public static String getServerName() {
        return getValueFromConf("host");
    }

    public static int getServerPort() {
        try {
            return Integer.parseInt(Objects.requireNonNull(getValueFromConf("port")));
        } catch (Exception e) {
            System.err.println("[ERROR] Hay un error al parsear el puerto a Integer (Es NULL, seguramente sea null)");
        }

        return 0;
    }
}