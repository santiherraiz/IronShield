package org.Config;

public class AppConfig {

    public static final int INACTIVITY_THRESHOLD_SECONDS = 30;
    
    /*
     * Intervalo de polling del monitor de inactividad en MILISEGUNDOS.
     * El sistema verificará guardias inactivos cada este tiempo.
     *
     */
    public static final int POLLING_INTERVAL_MS = 5000;
    
    /**
     * Tiempo en segundos para considerar un registro como "histórico"
     * y no mostrarlo en la lista de guardias activos.
     */
    public static final int HISTORY_CUTOFF_SECONDS = 3600; // 1 hora
    
    /**
     * Tamaño del pool de conexiones a la base de datos
     */
    public static final int DB_POOL_SIZE = 10;
    
    /**
     * Timeout de conexión en segundos
     */
    public static final int DB_CONNECTION_TIMEOUT = 30;

    /**
     * Timeout para las operaciones de red en milisegundos
     */
    public static final int NETWORK_TIMEOUT_MS = 10000;
    
    /**
     * Número máximo de reintentos para operaciones fallidas
     */
    public static final int MAX_RETRIES = 3;
    
    /**
     * Convierte el umbral de inactividad a minutos para visualización
     */
    public static double getInactivityThresholdMinutes() {
        return INACTIVITY_THRESHOLD_SECONDS / 60.0;
    }
    
    /**
     * Convierte el umbral de inactividad a horas para visualización
     */
    public static double getInactivityThresholdHours() {
        return INACTIVITY_THRESHOLD_SECONDS / 3600.0;
    }
    
    /**
     * Obtiene el intervalo de polling en segundos
     */
    public static int getPollingIntervalSeconds() {
        return POLLING_INTERVAL_MS / 1000;
    }
    
    /**
     * Información de configuración para logging
     */
    public static String getConfigInfo() {
        return String.format(
            "TrackMate Configuration:%n" +
            "  - Umbral de inactividad: %d segundos (%.2f minutos)%n" +
            "  - Intervalo de polling: %d ms (%d segundos)%n" +
            "  - Corte histórico: %d segundos%n" +
            "  - Pool de BD: %d conexiones%n",
            INACTIVITY_THRESHOLD_SECONDS,
            getInactivityThresholdMinutes(),
            POLLING_INTERVAL_MS,
            getPollingIntervalSeconds(),
            HISTORY_CUTOFF_SECONDS,
            DB_POOL_SIZE
        );
    }
}