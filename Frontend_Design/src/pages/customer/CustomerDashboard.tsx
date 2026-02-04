import React from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { KPICard } from '@/components/KPICard';
import { BookingCard } from '@/components/BookingCard';
import { FlightCard } from '@/components/FlightCard';
import { Button } from '@/components/ui/button';
import { mockBookings, mockFlights } from '@/data/mockData';
import { Plane, CalendarCheck, Award, Heart, Search, ArrowRight } from 'lucide-react';

interface CustomerDashboardProps {
  onNavigate: (page: string) => void;
}

export const CustomerDashboard: React.FC<CustomerDashboardProps> = ({ onNavigate }) => {
  const { user } = useAuth();
  
  // Get user's bookings (in real app, filter by user ID)
  const upcomingBookings = mockBookings
    .filter((b) => b.status === 'confirmed')
    .slice(0, 2);
  
  const featuredFlights = mockFlights.slice(0, 3);

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Welcome Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-foreground">
            Welcome back, {user?.name?.split(' ')[0]}! ✈️
          </h1>
          <p className="text-muted-foreground">Ready for your next adventure?</p>
        </div>
        <Button variant="sky" size="lg" onClick={() => onNavigate('search')}>
          <Search className="w-4 h-4 mr-2" />
          Search Flights
        </Button>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <KPICard
          title="Upcoming Flights"
          value={upcomingBookings.length}
          icon={Plane}
          accentColor="sky"
        />
        <KPICard
          title="Total Bookings"
          value={5}
          icon={CalendarCheck}
          accentColor="navy"
        />
        <KPICard
          title="Reward Points"
          value="2,500"
          icon={Award}
          accentColor="warning"
        />
        <KPICard
          title="Saved Flights"
          value={3}
          icon={Heart}
          accentColor="success"
        />
      </div>

      {/* Quick Search */}
      <div className="bg-gradient-to-r from-primary to-navy-light rounded-2xl p-6 text-primary-foreground">
        <div className="flex flex-col md:flex-row md:items-end gap-4">
          <div className="flex-1 grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="text-sm opacity-80 block mb-2">From</label>
              <input
                type="text"
                placeholder="Origin city"
                className="w-full h-12 px-4 rounded-lg bg-white/10 border border-white/20 text-primary-foreground placeholder:text-white/50 focus:outline-none focus:ring-2 focus:ring-accent"
              />
            </div>
            <div>
              <label className="text-sm opacity-80 block mb-2">To</label>
              <input
                type="text"
                placeholder="Destination city"
                className="w-full h-12 px-4 rounded-lg bg-white/10 border border-white/20 text-primary-foreground placeholder:text-white/50 focus:outline-none focus:ring-2 focus:ring-accent"
              />
            </div>
            <div>
              <label className="text-sm opacity-80 block mb-2">Date</label>
              <input
                type="date"
                className="w-full h-12 px-4 rounded-lg bg-white/10 border border-white/20 text-primary-foreground focus:outline-none focus:ring-2 focus:ring-accent"
              />
            </div>
          </div>
          <Button
            variant="hero"
            size="lg"
            className="md:w-auto w-full"
            onClick={() => onNavigate('search')}
          >
            Search Flights
            <ArrowRight className="w-4 h-4 ml-2" />
          </Button>
        </div>
      </div>

      {/* Upcoming Trips */}
      {upcomingBookings.length > 0 && (
        <div>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-foreground">Upcoming Trips</h2>
            <Button variant="ghost" onClick={() => onNavigate('bookings')}>
              View all
              <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </div>
          <div className="space-y-4">
            {upcomingBookings.map((booking) => (
              <BookingCard key={booking.id} booking={booking} />
            ))}
          </div>
        </div>
      )}

      {/* Featured Flights */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-foreground">Featured Flights</h2>
          <Button variant="ghost" onClick={() => onNavigate('search')}>
            See more
            <ArrowRight className="w-4 h-4 ml-1" />
          </Button>
        </div>
        <div className="space-y-4">
          {featuredFlights.map((flight) => (
            <FlightCard
              key={flight.id}
              flight={flight}
              onSelect={() => onNavigate('search')}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
