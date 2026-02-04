export type UserRole = 'customer' | 'admin';

export interface User {
  id: string;
  email: string;
  name: string;
  role: UserRole;
  phone?: string;
  createdAt: Date;
}

export interface Flight {
  id: string;
  flightNumber: string;
  airline: string;
  origin: string;
  originCode: string;
  destination: string;
  destinationCode: string;
  departureTime: Date;
  arrivalTime: Date;
  duration: string;
  basePrice: number;
  dynamicPrice: number;
  capacity: number;
  bookedSeats: number;
  status: 'scheduled' | 'delayed' | 'cancelled' | 'completed';
}

export interface Booking {
  id: string;
  reference: string;
  customerId: string;
  customerName: string;
  flightId: string;
  flight: Flight;
  passengers: number;
  totalPrice: number;
  status: 'pending' | 'confirmed' | 'cancelled' | 'completed';
  bookedAt: Date;
  specialRequests?: string;
}

export interface Customer {
  id: string;
  name: string;
  email: string;
  phone: string;
  rewardPoints: number;
  tier: 'bronze' | 'silver' | 'gold' | 'platinum';
  totalBookings: number;
  createdAt: Date;
  isDeleted?: boolean;
}

export interface Notification {
  id: string;
  type: 'booking' | 'flight_update' | 'promo' | 'system';
  title: string;
  message: string;
  isRead: boolean;
  createdAt: Date;
}

export interface DashboardStats {
  totalFlights: number;
  totalCustomers: number;
  todayBookings: number;
  monthlyRevenue: number;
  upcomingFlights: number;
  cancelledBookings: number;
}
