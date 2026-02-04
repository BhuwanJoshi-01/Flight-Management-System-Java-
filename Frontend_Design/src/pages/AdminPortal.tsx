import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { AdminDashboard } from './admin/AdminDashboard';
import { AdminFlights } from './admin/AdminFlights';
import { AdminCustomers } from './admin/AdminCustomers';
import { AdminBookings } from './admin/AdminBookings';
import { AdminRevenue } from './admin/AdminRevenue';
import { AdminSettings } from './admin/AdminSettings';

export const AdminPortal: React.FC = () => {
  const [currentPage, setCurrentPage] = useState('dashboard');

  const renderPage = () => {
    switch (currentPage) {
      case 'dashboard':
        return <AdminDashboard onNavigate={setCurrentPage} />;
      case 'flights':
        return <AdminFlights />;
      case 'customers':
        return <AdminCustomers />;
      case 'bookings':
        return <AdminBookings />;
      case 'revenue':
        return <AdminRevenue />;
      case 'settings':
        return <AdminSettings />;
      default:
        return <AdminDashboard onNavigate={setCurrentPage} />;
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
