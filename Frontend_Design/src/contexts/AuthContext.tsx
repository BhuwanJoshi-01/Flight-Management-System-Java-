import React, { createContext, useContext, useState, useCallback } from 'react';
import { User, UserRole } from '@/types';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  login: (email: string, password: string, role: UserRole) => Promise<boolean>;
  register: (name: string, email: string, phone: string, password: string) => Promise<boolean>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Mock users for demo
const mockUsers: Record<string, { password: string; user: User }> = {
  'admin@flights.com': {
    password: 'admin123',
    user: {
      id: '1',
      email: 'admin@flights.com',
      name: 'System Admin',
      role: 'admin',
      createdAt: new Date(),
    },
  },
  'john@example.com': {
    password: 'password123',
    user: {
      id: '2',
      email: 'john@example.com',
      name: 'John Smith',
      role: 'customer',
      phone: '+44 7700 900123',
      createdAt: new Date(),
    },
  },
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  const login = useCallback(async (email: string, password: string, role: UserRole): Promise<boolean> => {
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 800));

    const userData = mockUsers[email.toLowerCase()];
    if (userData && userData.password === password && userData.user.role === role) {
      setUser(userData.user);
      return true;
    }
    return false;
  }, []);

  const register = useCallback(async (name: string, email: string, phone: string, password: string): Promise<boolean> => {
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 800));

    if (mockUsers[email.toLowerCase()]) {
      return false; // Email already exists
    }

    const newUser: User = {
      id: Date.now().toString(),
      email: email.toLowerCase(),
      name,
      phone,
      role: 'customer',
      createdAt: new Date(),
    };

    mockUsers[email.toLowerCase()] = { password, user: newUser };
    setUser(newUser);
    return true;
  }, []);

  const logout = useCallback(() => {
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
