import React from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';

interface Props {
  navigation: any;
}

const DATOS_REGISTRO = [
  { id: '1', tipo: 'PUNTO DE CONTROL DELTA', hora: '23:40', estado: 'OK' },
  { id: '2', tipo: 'PUNTO DE CONTROL ECHO', hora: '23:25', estado: 'OK' },
  { id: '3', tipo: 'PUNTO DE CONTROL FOXTROT', hora: '23:10', estado: 'OK' },
];

const PantallaDetalleGuardia = ({ navigation }: Props) => {

  const handleCerrar = () => {
    if (navigation.canGoBack()) {
      navigation.goBack();
    } else {
      navigation.navigate('Dashboard'); 
    }
  };

  return (
    <SafeAreaView style={styles.contenedor}>
      <View style={styles.encabezado}>
        <Text style={styles.tituloPantalla}>PANEL DE DETALLE DEL GUARDIA</Text>
      </View>

      <View style={styles.cuadriculaInfo}>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>ID</Text>
          <Text style={styles.valor}>G002</Text>
        </View>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>NOMBRE</Text>
          <Text style={styles.valor}>RODRÍGUEZ, A.</Text>
        </View>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>INICIO TURNO</Text>
          <Text style={styles.valor}>22:00</Text>
        </View>
        <View style={styles.cajaInfo}>
          <Text style={styles.etiqueta}>ÚLT. MOV.</Text>
          <Text style={[styles.valor, { color: COLORS.primary }]}>30s INACTIVIDAD</Text>
        </View>
      </View>

      <View style={styles.seccionRegistros}>
        <Text style={styles.encabezadoSeccion}>REGISTRO DE PUNTOS DE CONTROL</Text>
        
        <FlatList
          data={DATOS_REGISTRO}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <View style={styles.itemRegistro}>
              <View style={styles.indicadorRegistro} />
              <View style={{ flex: 1 }}>
                <Text style={styles.tipoRegistro}>{item.tipo}</Text>
              </View>
              <Text style={styles.horaRegistro}>{item.hora}</Text>
            </View>
          )}
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