export const useServer = () => {
    const SERVER_URL = "http://172.30.77.59:45678";
    const SERVER_CASA = "http://192.168.1.38:45678" // Esto es para MI casa, cambiad a la vuestra si probáis: XAVI
    const SERVER_SANTI = "http://10.183.148.33:45678" // Esto es para SANTI
    /**
     * <strong>sendLocation</strong> envía la localización de la aplicación al servidor web. Primero comprobará si está en web o en
     * una plataforma, ya que es otra ruta distinta a la del emulador.
     * @param username El nombre de usuario
     * @param latitude La latitud
     * @param longitude La longitud
     */
    const sendLocation = async (username: string, latitude: string, longitude: string) => {
        try {
            await fetch(`${SERVER_SANTI}/location`, {
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
            const res = await fetch(`${SERVER_SANTI}/name`, {
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

    /**
     * <strong>getAlerts</strong> recibe de la BBDD las alertas registradas en la misma.
     */
    const getAlertsLog = async () => {
        // Esta consulta al servidor solo estará en el WebServer, ya que no tiene mucha lógica envíarla al TCP.
        try {
            const res = await fetch(`${SERVER_SANTI}/alert-log`);
            if (!res.ok) console.log("No se ha podido obtener los logs de alertas");
            return await res.json();
        } catch (error) {
            console.log(error);
        }
    }

    return {
        sendLocation,
        getName,
        getAlertsLog,
    }
}