import React, { useState } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { useGuardDetailScreen } from '../../hooks/useGuardDetailScreen';
import { useUser } from '../../contexts/UserContext';
import { useRoute } from "@react-navigation/core";
import { useServer } from '../../hooks/useServer';

type Agent = {
  username: string;
  latitude: number;
  longitude: number;
  date: string;
};

const PantallaDetalleGuardia = () => {

  const { handleCerrar } = useGuardDetailScreen();
  const { userId } = useUser();
  const route = useRoute();
  const { name } = route.params as { name: string };
  const { getAlertsLog } = useServer();

  // Alertas y su estado de carga
  const [alerts, setAlerts] = useState<Agent[]>([]);
  const [loading, setLoading] = useState(false);

  /**
   * La función <strong>cargarAlertas</strong> es una función asíncrona la cual, como dice el nombre carga las alertas.
   * Esto lo hace mediante la llamada a la función <strong>getAlertsLog</strong>. Comprueba si es un array y lo asigna a
   * la variable de estado <strong>alerts</strong>
   */
  const cargarAlertas = async () => {
    setLoading(true);
    try {
      const data = await getAlertsLog();
      if (Array.isArray(data)) {
        // Aseguramos que estén ordenadas por fecha descendente (la más reciente primero)
        const sortedData = data.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
        setAlerts(sortedData as Agent[]);
      } else {
        console.log('[WARN] getAlertsLog no ha devuelto lo esperado', data);
      }
    } catch (e) {
      console.log('[ERROR] No se pudieron cargar alertas', e);
    } finally {
      setLoading(false);
    }
  };

  const getDistanceFromLatLonInMeters = (lat1: number, lon1: number, lat2: number, lon2: number) => {
    const R = 6371e3; // Radio de la tierra en metros
    const dLat = deg2rad(lat2 - lat1);
    const dLon = deg2rad(lon2 - lon1);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  };

  const deg2rad = (deg: number) => {
    return deg * (Math.PI / 180);
  };

  /**
   * La función <strong>renderAlert</strong> es el componente renderizable, pero sin ser componente y siendo una función
   * Esto fomatea la hora (que pasa como string) y la parsea la interfaz {@link Date}. Después hace el componente que se
   * compone del username, latitud, longitud y, por supuesto, la hora formateada.
   * <h1>SE PUEDE EXTRAER A UN COMPONENTE APARTE</h1>
   * @param item
   */
  const renderAlert = ({ item, index }: { item: Agent, index: number }) => {
    const hora = new Date(item.date).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
    });

    let isInactive = false;

    if (index < alerts.length - 1) {
      const prevItem = alerts[index + 1];

      const timeDiff = new Date(item.date).getTime() - new Date(prevItem.date).getTime();
      const distance = getDistanceFromLatLonInMeters(
        item.latitude, item.longitude,
        prevItem.latitude, prevItem.longitude
      );

      // CONDICIÓN: Si pasaron >= 30 segundos (30000ms) y se movió menos de 10 metros
      if (timeDiff >= 30000 && distance < 10) {
        isInactive = true;
      }
    }

    const statusColor = isInactive ? COLORS.danger : COLORS.success;

    return (
      <View style={styles.itemRegistro}>
        <View style={styles.indicadorRegistro} />
        <View style={{ flex: 1 }}>
          <Text style={styles.tipoRegistro}>
            {item.username.toUpperCase()} ({item.latitude.toFixed(3)}, {item.longitude.toFixed(3)})
          </Text>
        </View>
        <Text style={styles.horaRegistro}>{hora}</Text>
      </View>
    );
  };

  return (
    <SafeAreaView style={styles.contenedor}>
      <View style={styles.encabezado}>
        <Text style={styles.tituloPantalla}>PANEL DE DETALLE DEL GUARDIA</Text>
      </View>

      <View style={styles.cuadriculaInfo}>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>ID</Text>
          <Text style={styles.valor}>{userId}</Text>
        </View>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>NOMBRE</Text>
          <Text style={styles.valor}>{name.toUpperCase()}</Text>
        </View>
      </View>

      {/* Botón para cargar alertas */}
      <TouchableOpacity
        style={[styles.botonCerrar, { marginBottom: 10 }]}
        onPress={cargarAlertas}
      >
        <Text style={styles.textoBotonCerrar}>
          {loading ? 'CARGANDO...' : 'CARGAR ALERTAS'}
        </Text>
      </TouchableOpacity>

      {/* FlatList de alertas */}
      <View style={styles.seccionRegistros}>
        <Text style={styles.encabezadoSeccion}>ALERTAS RECIENTES</Text>

        <FlatList
          data={alerts}
          keyExtractor={(item) => `${item.username}-${item.date}`}
          renderItem={renderAlert}
          ListEmptyComponent={
            !loading && <Text style={{ color: COLORS.text, textAlign: 'center' }}>
              No hay alertas registradas
            </Text>
          }
        />
      </View>

      <TouchableOpacity style={styles.botonCerrar} onPress={handleCerrar}>
        <Text style={styles.textoBotonCerrar}>CERRAR PANEL</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: 10,
  },
  encabezado: {
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    marginBottom: 10,
  },
  tituloPantalla: {
    color: COLORS.primary,
    fontSize: 16,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  cuadriculaInfo: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginBottom: 20,
  },
  cajaInfo: {
    width: '48%',
    backgroundColor: 'transparent',
    borderWidth: 1,
    borderColor: COLORS.text,
    padding: 10,
    marginBottom: 10,
  },
  etiqueta: {
    color: COLORS.primary,
    fontSize: 10,
    marginBottom: 5,
  },
  valor: {
    color: COLORS.textHighlight,
    fontSize: 14,
    fontWeight: 'bold',
  },
  seccionRegistros: {
    flex: 1,
    borderWidth: 1,
    borderColor: COLORS.border,
    padding: 10,
  },
  encabezadoSeccion: {
    color: COLORS.primary,
    marginBottom: 10,
    fontSize: 12,
  },
  itemRegistro: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#000',
    marginBottom: 5,
    padding: 10,
    borderLeftWidth: 3,
    borderLeftColor: COLORS.success,
  },
  indicadorRegistro: {
    width: 8,
    height: 8,
    backgroundColor: COLORS.success,
    marginRight: 10,
  },
  tipoRegistro: {
    color: COLORS.text,
    fontSize: 12,
  },
  horaRegistro: {
    color: COLORS.textHighlight,
    fontSize: 12,
    fontFamily: 'monospace',
  },
  botonCerrar: {
    backgroundColor: COLORS.primary,
    padding: 15,
    marginTop: 10,
    alignItems: 'center',
  },
  textoBotonCerrar: {
    color: 'black',
    fontWeight: 'bold',
  }
});

export default PantallaDetalleGuardia;