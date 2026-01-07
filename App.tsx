import React from 'react';
import { StatusBar } from 'react-native';
import AppNavigator from './app/navigation/AppNavigator'; 
import { COLORS } from './constants/colors';

export default function App() {
  return (
    <>
      <StatusBar 
        barStyle="light-content" 
        backgroundColor={COLORS.background} 
      />
      <AppNavigator />
    </>
  );
}