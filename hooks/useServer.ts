import {Platform} from "react-native";

export const useServer = () => {
    const handleConn = async (latitude: string, longitude: string) => {
        const SERVER_URL = Platform.OS === "web"
            ? "http://localhost:45678"
            : "http://10.0.2.2:45678";
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


    return {
        handleConn,
    }
}