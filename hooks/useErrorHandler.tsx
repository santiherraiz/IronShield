import { useEffect } from 'react';
import { sendErrorToJava } from '../app/services/LogService';

/**
 * Hook para capturar y enviar errores a Java
 */
export const useErrorHandler = (componentName: string) => {
  useEffect(() => {
    const handleError = (event: ErrorEvent) => {
      console.error(`[${componentName}] Error:`, event.error);
      sendErrorToJava(event.message, componentName, {
        file: event.filename,
        line: event.lineno,
      });
    };

    window.addEventListener('error', handleError);
    return () => window.removeEventListener('error', handleError);
  }, [componentName]);

  return { sendErrorToJava };
};
