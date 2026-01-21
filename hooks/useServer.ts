import {Platform} from "react-native";

export const useServer = () => {
    /**
     * <strong>sendLocation</strong> envía la localización de la aplicación al servidor web. Primero comprobará si está en web o en
     * una plataforma, ya que es otra ruta distinta a la del emulador.
     * @param latitude La latitud
     * @param longitude La longitud
     */
    const sendLocation = async (latitude: string, longitude: string) => {
        const SERVER_URL = "http://172.30.77.54:45678";
        try {
            await fetch(`${SERVER_URL}/location`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ latitude, longitude }),
            });
        } catch (error) {
            console.log(error);
        }
    };

    // Aquí va la función para recibir los registros en el futuro
    return {
        sendLocation,
    }
}