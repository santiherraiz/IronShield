const fs = require('fs');
const os = require('os');
const path = require('path');

// 1. Obtener la IP Local de forma agnóstica al OS
function getLocalIp() {
    const interfaces = os.networkInterfaces();
    for (const name of Object.keys(interfaces)) {
        for (const iface of interfaces[name]) {
            // Saltamos las internas (127.0.0.1) y las que no sean IPv4
            if ('IPv4' !== iface.family || iface.internal) {
                continue;
            }
            return iface.address;
        }
    }
    return '127.0.0.1';
}

const myIp = getLocalIp();
console.log(`IP Detectada: ${myIp}`);

// 2. Ruta del archivo a actualizar
const hooksPath = path.join(__dirname, 'hooks', 'useServer.ts');

// 3. Leer y reemplazar
try {
    if (fs.existsSync(hooksPath)) {
        let content = fs.readFileSync(hooksPath, 'utf8');

        // Regex flexible para reemplazar la línea de BASE_URL (con o sin puerto/placeholder)
        content = content.replace(/const BASE_URL = "http:\/\/.*";/, `const BASE_URL = "http://${myIp}:45678";`);

        fs.writeFileSync(hooksPath, content);
        console.log("✅ hooks/useServer.ts actualizado correctamente.");
    } else {
        console.error("No se encontró hooks/useServer.ts");
    }
} catch (error) {
    console.error("Error actualizando useServer.ts:", error);
}