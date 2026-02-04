import React from 'react';
import { KPICard } from '@/components/KPICard';
import { mockDashboardStats, mockBookings, mockFlights } from '@/data/mockData';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { 
  Plane, 
  Users, 
  CalendarCheck, 
  TrendingUp, 
  Clock, 
  XCircle,
  ArrowRight,
  AlertTriangle
} from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';

interface AdminDashboardProps {
  onNavigate: (page: string) => void;
}

const statusColors = {
  confirmed: 'bg-success/10 text-success border-success/20',
  pending: 'bg-warning/10 text-warning border-warning/20',
  cancelled: 'bg-destructive/10 text-destructive border-destructive/20',
};

export const AdminDashboard: React.FC<AdminDashboardProps> = ({ onNavigate }) => {
  const stats = mockDashboardStats;
  const recentBookings = mockBookings.slice(0, 5);
  const lowOccupancyFlights = mockFlights.filter(
    (f) => (f.bookedSeats / f.capacity) * 100 < 50
  );

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="text-2xl font-bold text-foreground">Dashboard</h1>
        <p className="text-muted-foreground">Overview of your flight booking system</p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        <KPICard
          title="Total Flights"
          value={stats.totalFlights}
          icon={Plane}
          accentColor="sky"
          trend={{ value: 12, isPositive: true }}
        />
        <KPICard
          title="Total Customers"
          value={stats.totalCustomers}
          icon={Users}
          accentColor="success"
          trend={{ value: 8, isPositive: true }}
        />
        <KPICard
          title="Today's Bookings"
          value={stats.todayBookings}
          icon={CalendarCheck}
          accentColor="navy"
        />
        <KPICard
          title="Monthly Revenue"
          value={`£${stats.monthlyRevenue.toLocaleString()}`}
          icon={TrendingUp}
          accentColor="warning"
          trend={{ value: 15, isPositive: true }}
        />
        <KPICard
          title="Departing Soon"
          value={stats.upcomingFlights}
          icon={Clock}
          accentColor="sky"
        />
        <KPICard
          title="Cancelled"
          value={stats.cancelledBookings}
          icon={XCircle}
          accentColor="warning"
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Bookings */}
        <div className="lg:col-span-2 bg-card rounded-xl shadow-soft border border-border/50">
          <div className="flex items-center justify-between p-5 border-b border-border/50">
            <h2 className="font-semibold text-foreground">Recent Bookings</h2>
            <Button variant="ghost" size="sm" onClick={() => onNavigate('bookings')}>
              View all
              <ArrowRight className="w-4 h-4 ml-1" />
            </Button>
          </div>
          <div className="divide-y divide-border/50">
            {recentBookings.map((booking) => (
              <div key={booking.id} className="flex items-center justify-between p-4 hover:bg-muted/30 transition-colors">
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center">
                    <Plane className="w-5 h-5 text-accent" />
                  </div>
                  <div>
                    <p className="font-medium text-foreground">{booking.reference}</p>
                    <p className="text-sm text-muted-foreground">
                      {booking.customerName} • {booking.flight.flightNumber}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <div className="text-right">
                    <p className="font-medium text-foreground">£{booking.totalPrice}</p>
                    <p className="text-xs text-muted-foreground">
                      {format(new Date(booking.bookedAt), 'MMM d, HH:mm')}
                    </p>
                  </div>
                  <Badge className={cn('border', statusColors[booking.status])}>
                    {booking.status}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Alerts Section */}
        <div className="space-y-4">
          {/* Low Occupancy Flights */}
          <div className="bg-card rounded-xl shadow-soft border border-border/50">
            <div className="flex items-center gap-2 p-5 border-b border-border/50">
              <AlertTriangle className="w-5 h-5 text-warning" />
              <h2 className="font-semibold text-foreground">Low Occupancy Flights</h2>
            </div>
            <div className="p-4 space-y-3">
              {lowOccupancyFlights.length > 0 ? (
                lowOccupancyFlights.slice(0, 3).map((flight) => {
                  const occupancy = Math.round((flight.bookedSeats / flight.capacity) * 100);
                  return (
                    <div
                      key={flight.id}
                      className="flex items-center justify-between p-3 bg-warning/5 rounded-lg border border-warning/20"
                    >
                      <div>
                        <p className="font-medium text-foreground">{flight.flightNumber}</p>
                        <p className="text-xs text-muted-foreground">
                          {flight.originCode} → {flight.destinationCode}
                        </p>
                      </div>
                      <div className="text-right">
                        <p className="font-medium text-warning">{occupancy}%</p>
                        <p className="text-xs text-muted-foreground">occupied</p>
                      </div>
                    </div>
                  );
                })
              ) : (
                <p className="text-sm text-muted-foreground text-center py-4">
                  All flights have good occupancy!
                </p>
              )}
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-card rounded-xl shadow-soft border border-border/50 p-5">
            <h2 className="font-semibold text-foreground mb-4">Quick Actions</h2>
            <div className="space-y-2">
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() => onNavigate('flights')}
              >
                <Plane className="w-4 h-4 mr-2" />
                Add New Flight
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() => onNavigate('customers')}
              >
                <Users className="w-4 h-4 mr-2" />
                Add Customer
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() => onNavigate('bookings')}
              >
                <CalendarCheck className="w-4 h-4 mr-2" />
                New Booking
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
