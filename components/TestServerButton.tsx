import {useState} from "react";
import {Button, View, Text, Platform} from "react-native";

interface Props {
    latitude: string;
    longitude: string;
}

/**
 * <h1><strong>PROVISIONAL</strong></h1>
 * @constructor
 */
const TestServerButton = ({ latitude, longitude }: Props) => {
    const [status, setStatus] = useState("Sin conexión");

    const testConn = async () => {
        const SERVER_URL = Platform.OS === "web"
        ? "http://localhost:45678"
        : "http://10.0.2.2:45678";
        try {
            const res = await fetch(`${SERVER_URL}/`, {
                method: "GET",
            });

            const text = await res.text();
            setStatus(text);
        } catch (error) {
            setStatus(`[ERROR]`);
            console.log(error);
        }
    };

    return (
        <View style={{padding: 40}}>
            <Button title={"Probar conexión"} onPress={testConn} />
            <Text style={{marginTop: 20}}>{status}</Text>
        </View>
    );
};

export default TestServerButton;