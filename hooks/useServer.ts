export const useServer = () => {
    const SERVER_URL = "http://172.30.77.54:45678";
    /**
     * <strong>sendLocation</strong> envía la localización de la aplicación al servidor web. Primero comprobará si está en web o en
     * una plataforma, ya que es otra ruta distinta a la del emulador.
     * @param name El nombre
     * @param latitude La latitud
     * @param longitude La longitud
     */
    const sendLocation = async (name: string, latitude: string, longitude: string) => {
        try {
            await fetch(`${SERVER_URL}/location`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({code: 1, name, latitude, longitude }),
            });
        } catch (error) {
            console.log(error);
        }
    };

    /**
     * <strong>getName</strong> envía el usuario y la conrtaseña y recibe el nombre del usuario si está registrado en
     * la base de datos
     * @param username
     * @param pass
     */
    const getName = async (username: string, pass: string) => {
        try {
            const res = await fetch(`${SERVER_URL}/name`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, pass })
            });

            if (!res.ok) {
                console.log("No se ha podido pasar el nombre.");
            }

            return await res.json();
        } catch (error) {
            console.log(error);
        }
    }

    return {
        sendLocation,
        getName,
    }
}