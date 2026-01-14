package org.BBDD;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionBD {

    private String url;
    private String user;
    private String password;

    public ConexionBD() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("database.properties"));

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            //Cambio

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection conectar() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}