import { useNavigation } from 'expo-router';

export const useGuardDetailScreen = () => {

    const navigation = useNavigation<any>();

    const handleCerrar = () => {
        try {
            navigation.replace('Login');
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
        }
    };

    return {
        handleCerrar
    };
};