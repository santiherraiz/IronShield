import { useState } from 'react';
import { Alert } from 'react-native';
import { sendErrorToJava } from '../app/services/LogService';

export const useDashboardScreen = () => {

    const [estado, setEstado] = useState('NOMINAL');

    const gestionarPuntoControl = async () => {
        try {
            const horaActual = new Date().toLocaleTimeString();
            Alert.alert("PUNTO DE CONTROL", `Coordenadas y hora (${horaActual}) enviadas al servidor.`);
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            sendErrorToJava(msg, 'DashboardScreen');
        }
    };

    return {
        estado,
        setEstado,
        gestionarPuntoControl
    };
};