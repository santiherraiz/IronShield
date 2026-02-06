CREATE DATABASE IF NOT EXISTS monitorizacion;

USE monitorizacion;

CREATE TABLE IF NOT EXISTS guardia_posicion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    pass VARCHAR(100) NOT NULL,
    rol ENUM('GUARDIA', 'SUPERVISOR') NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS inactividad (
     id INT AUTO_INCREMENT PRIMARY KEY,
     usuario_id INT NOT NULL,
     fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );


/*Ejemplos de como son los inserts*/

INSERT INTO usuarios (nombre, username, pass, rol) VALUES ('Martínez', 'G006','1234','GUARDIA');

INSERT INTO guardia_posicion (username,latitud,longitud) VALUES ('Prueba', 34.000,-42.000);


/*En el caso que Martínez realize una actividad, así se registra en la tabla de inactividad, que luego con una subconsulta se sacan todos sus datos*/
INSERT INTO inactividad (usuario_id) VALUES (6);



SELECT
    g.latitud,
    g.longitud,
    u.username,
    i.fecha AS fecha_inactividad
FROM inactividad i
         JOIN usuarios u ON i.usuario_id = u.id
         JOIN guardia_posicion g ON u.username = g.username
WHERE g.id = (
    SELECT MAX(id)
    FROM guardia_posicion
    WHERE username = u.username
);



Select * from guardia_posicion;

Select * from inactividad;

Select * from usuarios;
