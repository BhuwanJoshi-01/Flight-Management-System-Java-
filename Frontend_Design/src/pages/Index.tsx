import React from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { AuthPage } from './AuthPage';
import { CustomerPortal } from './CustomerPortal';
import { AdminPortal } from './AdminPortal';

const Index: React.FC = () => {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <AuthPage />;
  }

  if (user?.role === 'admin') {
    return <AdminPortal />;
  }

  return <CustomerPortal />;
};

export default Index;
