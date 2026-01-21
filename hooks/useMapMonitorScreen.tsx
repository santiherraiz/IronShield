import React from 'react';
import { useNavigation } from 'expo-router';

export const useMapMonitorScreen = () => {

    const navigation = useNavigation<any>();

    const handleCerrar = () => {
        try {
            navigation.navigate('OperationsStack');
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
        }
    };

    return {
        handleCerrar
    };
}