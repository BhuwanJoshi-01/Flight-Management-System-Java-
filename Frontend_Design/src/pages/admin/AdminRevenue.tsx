import React from 'react';
import { KPICard } from '@/components/KPICard';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { TrendingUp, PoundSterling, CalendarCheck, Users, Download, BarChart3, PieChart } from 'lucide-react';
import { mockBookings, mockFlights } from '@/data/mockData';

export const AdminRevenue: React.FC = () => {
  // Calculate some basic stats
  const totalRevenue = mockBookings
    .filter((b) => b.status !== 'cancelled')
    .reduce((sum, b) => sum + b.totalPrice, 0);

  const confirmedBookings = mockBookings.filter((b) => b.status === 'confirmed').length;
  const avgBookingValue = totalRevenue / confirmedBookings || 0;

  // Revenue by route
  const revenueByRoute = mockBookings
    .filter((b) => b.status !== 'cancelled')
    .reduce((acc, b) => {
      const route = `${b.flight.originCode} → ${b.flight.destinationCode}`;
      acc[route] = (acc[route] || 0) + b.totalPrice;
      return acc;
    }, {} as Record<string, number>);

  const topRoutes = Object.entries(revenueByRoute)
    .sort(([, a], [, b]) => b - a)
    .slice(0, 5);

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Revenue & Analytics</h1>
          <p className="text-muted-foreground">Financial overview and insights</p>
        </div>
        <div className="flex gap-3">
          <Select defaultValue="month">
            <SelectTrigger className="w-32">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="week">This Week</SelectItem>
              <SelectItem value="month">This Month</SelectItem>
              <SelectItem value="quarter">This Quarter</SelectItem>
              <SelectItem value="year">This Year</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline">
            <Download className="w-4 h-4 mr-2" />
            Export Report
          </Button>
        </div>
      </div>

      {/* Revenue KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <KPICard
          title="Total Revenue"
          value={`£${totalRevenue.toLocaleString()}`}
          icon={PoundSterling}
          accentColor="success"
          trend={{ value: 15, isPositive: true }}
        />
        <KPICard
          title="Avg. Booking Value"
          value={`£${avgBookingValue.toFixed(0)}`}
          icon={TrendingUp}
          accentColor="sky"
          trend={{ value: 5, isPositive: true }}
        />
        <KPICard
          title="Total Bookings"
          value={confirmedBookings}
          icon={CalendarCheck}
          accentColor="navy"
        />
        <KPICard
          title="Active Customers"
          value={42}
          icon={Users}
          accentColor="warning"
          trend={{ value: 8, isPositive: true }}
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Revenue Chart Placeholder */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <BarChart3 className="w-5 h-5 text-accent" />
              Revenue Trend
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-64 flex items-center justify-center bg-muted/30 rounded-lg border border-dashed border-border">
              <div className="text-center text-muted-foreground">
                <BarChart3 className="w-12 h-12 mx-auto mb-3 opacity-50" />
                <p className="font-medium">Revenue Chart</p>
                <p className="text-sm">Chart visualization would appear here</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Top Routes by Revenue */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="w-5 h-5 text-accent" />
              Top Routes by Revenue
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {topRoutes.map(([route, revenue], index) => {
                const maxRevenue = topRoutes[0][1];
                const percentage = (revenue / maxRevenue) * 100;
                return (
                  <div key={route} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <span className="text-sm font-medium text-muted-foreground w-6">
                          #{index + 1}
                        </span>
                        <span className="font-medium">{route}</span>
                      </div>
                      <span className="font-bold">£{revenue.toLocaleString()}</span>
                    </div>
                    <div className="h-2 bg-muted rounded-full overflow-hidden">
                      <div
                        className="h-full bg-accent rounded-full transition-all"
                        style={{ width: `${percentage}%` }}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Booking Status Distribution */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <PieChart className="w-5 h-5 text-accent" />
              Booking Status Distribution
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-64 flex items-center justify-center">
              <div className="grid grid-cols-2 gap-8">
                {[
                  { label: 'Confirmed', count: mockBookings.filter((b) => b.status === 'confirmed').length, color: 'bg-success' },
                  { label: 'Pending', count: mockBookings.filter((b) => b.status === 'pending').length, color: 'bg-warning' },
                  { label: 'Cancelled', count: mockBookings.filter((b) => b.status === 'cancelled').length, color: 'bg-destructive' },
                  { label: 'Completed', count: mockBookings.filter((b) => b.status === 'completed').length, color: 'bg-muted-foreground' },
                ].map((item) => (
                  <div key={item.label} className="flex items-center gap-3">
                    <div className={`w-4 h-4 rounded-full ${item.color}`} />
                    <div>
                      <p className="font-medium">{item.label}</p>
                      <p className="text-2xl font-bold">{item.count}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Flight Occupancy */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Users className="w-5 h-5 text-accent" />
              Flight Occupancy Rates
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {mockFlights.slice(0, 5).map((flight) => {
                const occupancy = Math.round((flight.bookedSeats / flight.capacity) * 100);
                return (
                  <div key={flight.id} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div>
                        <span className="font-medium">{flight.flightNumber}</span>
                        <span className="text-sm text-muted-foreground ml-2">
                          {flight.originCode} → {flight.destinationCode}
                        </span>
                      </div>
                      <span className="font-bold">{occupancy}%</span>
                    </div>
                    <div className="h-2 bg-muted rounded-full overflow-hidden">
                      <div
                        className={`h-full rounded-full transition-all ${
                          occupancy >= 80 ? 'bg-success' : occupancy >= 50 ? 'bg-warning' : 'bg-destructive'
                        }`}
                        style={{ width: `${occupancy}%` }}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
