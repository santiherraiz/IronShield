package com.ironshield.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class XmlLogService {

    /**
     * Genera un archivo XML único para el error recibido.
     * @param errorData Datos enviados desde React (detail, location)
     */
    public void generateErrorXml(Map<String, String> errorData) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Elemento raíz: ErrorLog
            Element rootElement = doc.createElement("ErrorLog");
            doc.appendChild(rootElement);

            // Fecha y Hora
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Element time = doc.createElement("Timestamp");
            time.appendChild(doc.createTextNode(timestamp));
            rootElement.appendChild(time);

            // Ubicación del error (Componente React)
            Element location = doc.createElement("Location");
            location.appendChild(doc.createTextNode(errorData.getOrDefault("location", "Unknown")));
            rootElement.appendChild(location);

            // Detalle de la excepción
            Element detail = doc.createElement("Detail");
            detail.appendChild(doc.createTextNode(errorData.getOrDefault("detail", "No details provided")));
            rootElement.appendChild(detail);

            // Configurar el transformador para escribir el archivo físico
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Nombre de archivo único basado en el tiempo para no sobrescribir
            String fileName = "error_" + System.currentTimeMillis() + ".xml";
            File logDir = new File("logs");
            if (!logDir.exists()) logDir.mkdirs();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(logDir, fileName));

            transformer.transform(source, result);
            System.out.println("Archivo de log XML generado: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}