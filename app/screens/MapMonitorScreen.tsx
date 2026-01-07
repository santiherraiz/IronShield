import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';

const PantallaMonitoreoMapa = () => {
  return (
    <SafeAreaView style={styles.contenedor}>
      <View style={styles.barraSuperior}>
        <Text style={styles.textoZona}>ZONA: POLÍGONO NORTE</Text>
        <View style={styles.indicadorSenal}>
          <Text style={styles.textoSenal}>SEÑAL: FUERTE</Text>
        </View>
      </View>

      <View style={styles.marcadorMapa}>
        <Text style={{ color: COLORS.text }}>
          [COMPONENTE DE VISTA DE MAPA AQUÍ]
        </Text>
        <Text style={{ color: COLORS.text, marginTop: 10 }}>
          Lat: 39.4699 | Lon: -0.3763
        </Text>
      </View>

      <View style={styles.panelCoordenadas}>
        <Text style={styles.tituloPanel}>TELEMETRÍA EN VIVO</Text>
        <View style={styles.fila}>
          <Text style={styles.etiqueta}>VELOCIDAD:</Text>
          <Text style={styles.valor}>0 km/h</Text>
        </View>
        <View style={styles.fila}>
          <Text style={styles.etiqueta}>ÚLTIMO PING:</Text>
          <Text style={styles.valor}>hace 0s</Text>
        </View>
   
        <Text style={styles.textoAlerta}>SIN MOVIMIENTO DETECTADO (5s)</Text>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  barraSuperior: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: 15,
    backgroundColor: COLORS.card,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  textoZona: {
    color: COLORS.primary,
    fontWeight: 'bold',
  },
  indicadorSenal: {
    backgroundColor: COLORS.success,
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 2,
  },
  textoSenal: {
    color: 'black',
    fontSize: 10,
    fontWeight: 'bold',
  },
  marcadorMapa: {
    flex: 2,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#001020',
  },
  panelCoordenadas: {
    flex: 1,
    padding: 20,
    backgroundColor: COLORS.background,
    borderTopWidth: 2,
    borderTopColor: COLORS.primary,
  },
  tituloPanel: {
    color: COLORS.text,
    fontSize: 12,
    marginBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    paddingBottom: 5,
  },
  fila: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  etiqueta: {
    color: COLORS.text,
    fontFamily: 'monospace',
  },
  valor: {
    color: COLORS.textHighlight,
    fontWeight: 'bold',
    fontFamily: 'monospace',
  },
  textoAlerta: {
    color: COLORS.primary,
    marginTop: 10,
    textAlign: 'center',
    fontWeight: 'bold',
  }
});

export default PantallaMonitoreoMapa;