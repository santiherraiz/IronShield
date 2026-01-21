import { useNavigation } from 'expo-router';

export const useGuardDetailScreen = () => {

    const navigation = useNavigation<any>();

    const handleCerrar = () => {
        try {
            if (navigation.canGoBack()) {
                navigation.goBack();
            } else {
                navigation.navigate('Dashboard');
            }
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
        }
    };

    return {
        handleCerrar
    };
};