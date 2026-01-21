
import { useState } from 'react';
import { Alert } from 'react-native';
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