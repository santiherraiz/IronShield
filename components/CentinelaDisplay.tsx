import React from 'react';
import { View, Text, Button, ActivityIndicator, StyleSheet } from 'react-native';
import { useCentinela } from '../hooks/useCentinelaLocation';

export const CentinelaDisplay = () => {
    const {location, errorMsg, loading, refresh} = useCentinela();

    return (
    <View className="bg-black p-5 border-2 border-green-900 shadow-2xl">
      {/* Header de Estado */}
      <View className="flex-row items-center justify-between mb-6 border-b border-green-900 pb-3">
        <View className="flex-row items-center">
          <Radar size={18} color="#22c55e" />
          <Text className="text-green-500 font-mono text-xs ml-3 tracking-[2px] font-bold uppercase">
            Centinela V1 - En Línea
          </Text>
        </View>
        <TouchableOpacity onPress={refresh} disabled={loading}>
          <RefreshCw size={16} color={loading ? "#14532d" : "#22c55e"} />
        </TouchableOpacity>
      </View>

      {loading ? (
        <View className="py-10 items-center">
          <ActivityIndicator color="#22c55e" size="large" />
          <Text className="text-green-500 font-mono text-[10px] mt-4 uppercase animate-pulse">
            Sincronizando coordenadas...
          </Text>
        </View>
      ) : errorMsg ? (
        <View className="bg-red-950/30 border border-red-600 p-4 flex-row items-center">
          <AlertOctagon size={24} color="#dc2626" />
          <Text className="text-red-500 font-mono text-xs ml-3 font-black uppercase flex-1 leading-4">
            {errorMsg}
          </Text>
        </View>
      ) : (
        <View className="space-y-4">
          {/* Panel de Datos GPS */}
          <View className="bg-green-950/10 p-4 border border-green-900/40">
            <View className="flex-row justify-between mb-3">
              <Text className="text-green-800 font-mono text-[10px] uppercase font-bold">Latitud:</Text>
              <Text className="text-green-400 font-mono text-base font-black">
                {location?.coords.latitude.toFixed(6)}
              </Text>
            </View>
            <View className="flex-row justify-between">
              <Text className="text-green-800 font-mono text-[10px] uppercase font-bold">Longitud:</Text>
              <Text className="text-green-400 font-mono text-base font-black">
                {location?.coords.longitude.toFixed(6)}
              </Text>
            </View>
          </View>

          {/* Verificación de Integridad */}
          <View className="pt-3 border-t border-green-900/30 flex-row items-center justify-between">
            <View className="flex-row items-center">
              <ShieldCheck size={14} color="#166534" />
              <Text className="text-green-900 font-mono text-[9px] ml-2 uppercase">
                Precisión: {location?.coords.accuracy?.toFixed(1)} metros
              </Text>
            </View>
            <View className="bg-green-900/20 px-2 py-1">
              <Text className="text-green-500 font-mono text-[8px] font-bold">VERIFICADO</Text>
            </View>
          </View>
        </View>
      )}

      {/* Marca de Agua Disciplinaria */}
      <Text className="text-green-950 font-mono text-[8px] mt-6 text-center uppercase leading-3">
        Cualquier discrepancia en la ubicación será reportada al Comandante Raül de forma automática.
      </Text>
    </View>
  );

}