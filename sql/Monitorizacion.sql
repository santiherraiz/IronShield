
CREATE DATABASE IF NOT EXISTS monitorizacion;


USE monitorizacion;

CREATE TABLE IF NOT EXISTS guardia_posicion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_guardia VARCHAR(100) NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);git