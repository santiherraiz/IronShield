package org.BBDD;

import org.Server.LogManager;
import org.Server.UtilsServer;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private String url;
    private String user;
    private String password;

    public ConexionBD() {

        try {

            url = UtilsServer.getValueFromConf("db.url", "database.properties");
            user = UtilsServer.getValueFromConf("db.agent", "database.properties");
            password = UtilsServer.getValueFromConf("db.password", "database.properties");

        } catch (Exception e) {
            LogManager.error("[ERROR] No se pudo conectar a la base de datos", e);
        }
    }

    public Connection conectar() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            LogManager.error("[ERROR] No se pudo conectar a la base de datos", e);
            return null;
        }
    }
}