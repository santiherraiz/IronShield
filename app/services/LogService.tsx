/**
 * LogService - Envía errores a Java
 * Simple: cuando ocurre un error, lo manda al servidor Java
 * Java crea un archivo XML con los detalles del error
 */

const JAVA_SERVER = 'http://localhost:8080/log-error';

export const sendErrorToJava = async (
  errorMessage: string,
  componentName: string,
  errorDetails?: any
) => {
  try {
    await fetch(JAVA_SERVER, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        error: errorMessage,
        component: componentName,
        timestamp: new Date().toISOString(),
        details: errorDetails,
      }),
    });
    console.log(`[✓] Error enviado a Java: ${componentName}`);
  } catch (err) {
    console.error('[✗] No se conectó con Java:', err);
  }
};
