export const useServer = () => {
    // Esta URL es actualizada automáticamente por set-ip.js
    const BASE_URL = "http://192.168.1.43:45678";

    /**
     * <strong>sendLocation</strong> envía la localización de la aplicación al servidor web. Primero comprobará si está en web o en
     * una plataforma, ya que es otra ruta distinta a la del emulador.
     * @param username El nombre de usuario
     * @param latitude La latitud
     * @param longitude La longitud
     */
    const sendLocation = async (username: string, latitude: string, longitude: string) => {
        try {
            await fetch(`${BASE_URL}/location`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ code: 3, username, latitude, longitude }),
            });
        } catch (error) {
            console.log(error);
        }
    };

    /**
     * <strong>getName</strong> envía el usuario y la contraseña y recibe el nombre del usuario si está registrado en
     * la base de datos
     * @param username
     * @param pass
     */
    const getName = async (username: string, pass: string) => {
        try {
            const res = await fetch(`${BASE_URL}/name`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ code: 1, username, pass })
            });

            if (!res.ok) console.log("No se ha podido pasar el nombre.");

            return await res.json();
        } catch (error) {
            console.log(error);
        }
    }

    const getActiveGuards = async () => {
        try {
            const res = await fetch(`${BASE_URL}/guards`);
            if (!res.ok) console.log("No se ha podido obtener la lista de guardias");
            return await res.json();
        } catch (error) {
            console.log(error);
            return [];
        }
    }

    return {
        sendLocation,
        getName,
        getActiveGuards,
    }
}