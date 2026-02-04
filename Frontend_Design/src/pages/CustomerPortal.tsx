import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { CustomerDashboard } from './customer/CustomerDashboard';
import { CustomerSearchFlights } from './customer/CustomerSearchFlights';
import { CustomerBookings } from './customer/CustomerBookings';
import { CustomerProfile } from './customer/CustomerProfile';
import { CustomerNotifications } from './customer/CustomerNotifications';

export const CustomerPortal: React.FC = () => {
  const [currentPage, setCurrentPage] = useState('dashboard');

  const renderPage = () => {
    switch (currentPage) {
      case 'dashboard':
        return <CustomerDashboard onNavigate={setCurrentPage} />;
      case 'search':
        return <CustomerSearchFlights onNavigate={setCurrentPage} />;
      case 'bookings':
        return <CustomerBookings />;
      case 'profile':
        return <CustomerProfile />;
      case 'notifications':
        return <CustomerNotifications />;
      default:
        return <CustomerDashboard onNavigate={setCurrentPage} />;
    }
  };

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar currentPage={currentPage} onNavigate={setCurrentPage} />
      <main className="flex-1 p-8 overflow-auto">
        {renderPage()}
      </main>
    </div>
  );
};
