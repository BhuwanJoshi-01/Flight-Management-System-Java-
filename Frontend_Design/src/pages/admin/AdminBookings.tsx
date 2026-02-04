import React, { useState } from 'react';
import { mockBookings } from '@/data/mockData';
import { Booking } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Checkbox } from '@/components/ui/checkbox';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Search, Eye, Edit, XCircle, Plane, Download, Filter } from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';
import { Label } from '@/components/ui/label';

const statusColors = {
  confirmed: 'bg-success/10 text-success border-success/20',
  pending: 'bg-warning/10 text-warning border-warning/20',
  cancelled: 'bg-destructive/10 text-destructive border-destructive/20',
  completed: 'bg-muted text-muted-foreground border-border',
};

export const AdminBookings: React.FC = () => {
  const [bookings, setBookings] = useState<Booking[]>(mockBookings);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [showCancelled, setShowCancelled] = useState(true);
  const [selectedBooking, setSelectedBooking] = useState<Booking | null>(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);

  const filteredBookings = bookings.filter((b) => {
    const matchesSearch =
      b.reference.toLowerCase().includes(searchTerm.toLowerCase()) ||
      b.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      b.flight.flightNumber.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesStatus = statusFilter === 'all' || b.status === statusFilter;
    const matchesCancelled = showCancelled || b.status !== 'cancelled';

    return matchesSearch && matchesStatus && matchesCancelled;
  });

  const handleCancel = (booking: Booking) => {
    setSelectedBooking(booking);
    setShowCancelModal(true);
  };

  const confirmCancel = () => {
    if (selectedBooking) {
      setBookings(
        bookings.map((b) =>
          b.id === selectedBooking.id ? { ...b, status: 'cancelled' as const } : b
        )
      );
      setShowCancelModal(false);
      setSelectedBooking(null);
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Bookings Management</h1>
          <p className="text-muted-foreground">View and manage all flight bookings</p>
        </div>
        <Button variant="outline">
          <Download className="w-4 h-4 mr-2" />
          Export Report
        </Button>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            placeholder="Search by reference, customer, or flight..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
        <Select value={statusFilter} onValueChange={setStatusFilter}>
          <SelectTrigger className="w-40">
            <SelectValue placeholder="Status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Status</SelectItem>
            <SelectItem value="confirmed">Confirmed</SelectItem>
            <SelectItem value="pending">Pending</SelectItem>
            <SelectItem value="cancelled">Cancelled</SelectItem>
            <SelectItem value="completed">Completed</SelectItem>
          </SelectContent>
        </Select>
        <div className="flex items-center gap-2">
          <Checkbox
            id="showCancelled"
            checked={showCancelled}
            onCheckedChange={(checked) => setShowCancelled(checked as boolean)}
          />
          <Label htmlFor="showCancelled" className="text-sm cursor-pointer">
            Show cancelled
          </Label>
        </div>
      </div>

      {/* Bookings Table */}
      <div className="bg-card rounded-xl shadow-soft border border-border/50 overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/30">
              <TableHead>Reference</TableHead>
              <TableHead>Customer</TableHead>
              <TableHead>Flight</TableHead>
              <TableHead>Date</TableHead>
              <TableHead>Passengers</TableHead>
              <TableHead>Total</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredBookings.map((booking) => (
              <TableRow key={booking.id} className="hover:bg-muted/20">
                <TableCell>
                  <p className="font-medium">{booking.reference}</p>
                  <p className="text-xs text-muted-foreground">
                    {format(new Date(booking.bookedAt), 'MMM d, yyyy')}
                  </p>
                </TableCell>
                <TableCell>
                  <p className="font-medium">{booking.customerName}</p>
                </TableCell>
                <TableCell>
                  <div className="flex items-center gap-2">
                    <Plane className="w-4 h-4 text-accent" />
                    <div>
                      <p className="font-medium">{booking.flight.flightNumber}</p>
                      <p className="text-xs text-muted-foreground">
                        {booking.flight.originCode} → {booking.flight.destinationCode}
                      </p>
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <p>{format(new Date(booking.flight.departureTime), 'MMM d, yyyy')}</p>
                  <p className="text-xs text-muted-foreground">
                    {format(new Date(booking.flight.departureTime), 'HH:mm')}
                  </p>
                </TableCell>
                <TableCell>
                  <p className="font-medium">{booking.passengers}</p>
                </TableCell>
                <TableCell>
                  <p className="font-bold">£{booking.totalPrice.toFixed(2)}</p>
                </TableCell>
                <TableCell>
                  <Badge className={cn('border capitalize', statusColors[booking.status])}>
                    {booking.status}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className="flex items-center justify-end gap-1">
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => {
                        setSelectedBooking(booking);
                        setShowDetailsModal(true);
                      }}
                    >
                      <Eye className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="icon">
                      <Edit className="w-4 h-4" />
                    </Button>
                    {booking.status !== 'cancelled' && (
                      <Button
                        variant="ghost"
                        size="icon"
                        className="text-destructive hover:text-destructive hover:bg-destructive/10"
                        onClick={() => handleCancel(booking)}
                      >
                        <XCircle className="w-4 h-4" />
                      </Button>
                    )}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* Booking Details Modal */}
      <Dialog open={showDetailsModal} onOpenChange={setShowDetailsModal}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>Booking Details</DialogTitle>
            <DialogDescription>Reference: {selectedBooking?.reference}</DialogDescription>
          </DialogHeader>
          {selectedBooking && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Customer</p>
                  <p className="font-medium">{selectedBooking.customerName}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Status</p>
                  <Badge className={cn('border capitalize', statusColors[selectedBooking.status])}>
                    {selectedBooking.status}
                  </Badge>
                </div>
                <div>
                  <p className="text-muted-foreground">Flight</p>
                  <p className="font-medium">{selectedBooking.flight.flightNumber}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Route</p>
                  <p className="font-medium">
                    {selectedBooking.flight.originCode} → {selectedBooking.flight.destinationCode}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Date</p>
                  <p className="font-medium">
                    {format(new Date(selectedBooking.flight.departureTime), 'PPP')}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Time</p>
                  <p className="font-medium">
                    {format(new Date(selectedBooking.flight.departureTime), 'HH:mm')} -{' '}
                    {format(new Date(selectedBooking.flight.arrivalTime), 'HH:mm')}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Passengers</p>
                  <p className="font-medium">{selectedBooking.passengers}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Booked On</p>
                  <p className="font-medium">
                    {format(new Date(selectedBooking.bookedAt), 'PPP')}
                  </p>
                </div>
              </div>
              <div className="pt-4 border-t">
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">Total Price</span>
                  <span className="text-xl font-bold">
                    £{selectedBooking.totalPrice.toFixed(2)}
                  </span>
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>

      {/* Cancel Confirmation Modal */}
      <Dialog open={showCancelModal} onOpenChange={setShowCancelModal}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Cancel Booking</DialogTitle>
            <DialogDescription>
              Are you sure you want to cancel booking {selectedBooking?.reference}?
            </DialogDescription>
          </DialogHeader>
          <div className="p-4 bg-warning/10 rounded-lg border border-warning/20 text-sm">
            <p className="font-medium text-warning">Warning</p>
            <p className="text-muted-foreground mt-1">
              This action will cancel the booking and initiate a refund process for the customer.
            </p>
          </div>
          <div className="flex gap-3 justify-end">
            <Button variant="outline" onClick={() => setShowCancelModal(false)}>
              Keep Booking
            </Button>
            <Button variant="destructive" onClick={confirmCancel}>
              Cancel Booking
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};
