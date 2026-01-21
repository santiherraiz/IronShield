import React from 'react';
import { StatusBar } from 'react-native';
import AppNavigator from './navigation/AppNavigator';
import { COLORS } from '../constants/colors';
import { UserProvider } from '../contexts/UserContext';

export default function App() {
  return (
    <UserProvider>
      <StatusBar 
        barStyle="light-content" 
        backgroundColor={COLORS.background} 
        translucent={false}
      />
      
      <AppNavigator />
    </UserProvider>
  );
}