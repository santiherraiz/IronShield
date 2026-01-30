package org.Config;

public class AppConfig {

    public static final int INACTIVITY_THRESHOLD_SECONDS = 30;
    
    /**
     * Intervalo de polling del monitor de inactividad en MILISEGUNDOS.
     * El sistema verificará guardias inactivos cada este tiempo.
     *
     */
    public static final int POLLING_INTERVAL_MS = 5000;
}