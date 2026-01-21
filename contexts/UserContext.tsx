import React, { createContext, useContext, useState, ReactNode } from 'react';

type UserRole = 'guardia' | 'supervisor' | null;

interface UserContextType {
  role: UserRole;
  setRole: (role: UserRole) => void;
  userId: string;
  setUserId: (id: string) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [role, setRole] = useState<UserRole>(null);
  const [userId, setUserId] = useState<string>('');

  return (
    <UserContext.Provider value={{ role, setRole, userId, setUserId }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};