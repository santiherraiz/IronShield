
import { useState } from 'react';
import { Alert } from 'react-native';
import { sendErrorToJava } from '../app/services/LogService';
import { useNavigation } from 'expo-router';

export const useLogin = () => {
    const navigation = useNavigation<any>();
    const [idServicio, setIdServicio] = useState('');
    const [contrasena, setContrasena] = useState('');

    const gestionarInicioSesion = async () => {
        try {
            if (!idServicio.trim() || !contrasena.trim()) {
                throw new Error('ID de servicio y contraseña son requeridos');
            }
            navigation.replace('OperationsStack');
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            sendErrorToJava(msg, 'LoginScreen');
            Alert.alert('Error', msg);
        }
    };

    return {
        idServicio,
        setIdServicio,
        contrasena,
        setContrasena,
        gestionarInicioSesion
    };
};