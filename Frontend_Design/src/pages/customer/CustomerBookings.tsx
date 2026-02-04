import React, { useState } from 'react';
import { BookingCard } from '@/components/BookingCard';
import { mockBookings } from '@/data/mockData';
import { Booking } from '@/types';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { CalendarCheck, Clock, XCircle, CheckCircle2 } from 'lucide-react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { format } from 'date-fns';

export const CustomerBookings: React.FC = () => {
  const [selectedBooking, setSelectedBooking] = useState<Booking | null>(null);
  const [showDetails, setShowDetails] = useState(false);
  const [showCancelConfirm, setShowCancelConfirm] = useState(false);

  // Filter bookings by status
  const upcomingBookings = mockBookings.filter(
    (b) => b.status === 'confirmed' || b.status === 'pending'
  );
  const completedBookings = mockBookings.filter((b) => b.status === 'completed');
  const cancelledBookings = mockBookings.filter((b) => b.status === 'cancelled');

  const handleView = (booking: Booking) => {
    setSelectedBooking(booking);
    setShowDetails(true);
  };

  const handleCancel = (booking: Booking) => {
    setSelectedBooking(booking);
    setShowCancelConfirm(true);
  };

  const handleDownload = (booking: Booking) => {
    // Simulate download
    console.log('Downloading e-ticket for:', booking.reference);
    alert(`E-Ticket for ${booking.reference} would be downloaded here.`);
  };

  const BookingsList = ({ bookings, emptyMessage }: { bookings: Booking[]; emptyMessage: string }) => (
    <div className="space-y-4">
      {bookings.length > 0 ? (
        bookings.map((booking) => (
          <BookingCard
            key={booking.id}
            booking={booking}
            onView={handleView}
            onCancel={handleCancel}
            onDownload={handleDownload}
          />
        ))
      ) : (
        <div className="text-center py-12 bg-card rounded-xl border border-border/50">
          <CalendarCheck className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
          <p className="text-muted-foreground">{emptyMessage}</p>
        </div>
      )}
    </div>
  );

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="text-2xl font-bold text-foreground">My Bookings</h1>
        <p className="text-muted-foreground">View and manage your flight bookings</p>
      </div>

      <Tabs defaultValue="upcoming" className="w-full">
        <TabsList className="grid w-full max-w-md grid-cols-3">
          <TabsTrigger value="upcoming" className="gap-2">
            <Clock className="w-4 h-4" />
            Upcoming ({upcomingBookings.length})
          </TabsTrigger>
          <TabsTrigger value="completed" className="gap-2">
            <CheckCircle2 className="w-4 h-4" />
            Completed ({completedBookings.length})
          </TabsTrigger>
          <TabsTrigger value="cancelled" className="gap-2">
            <XCircle className="w-4 h-4" />
            Cancelled ({cancelledBookings.length})
          </TabsTrigger>
        </TabsList>

        <TabsContent value="upcoming" className="mt-6">
          <BookingsList
            bookings={upcomingBookings}
            emptyMessage="You have no upcoming flights. Start exploring!"
          />
        </TabsContent>

        <TabsContent value="completed" className="mt-6">
          <BookingsList
            bookings={completedBookings}
            emptyMessage="No completed trips yet."
          />
        </TabsContent>

        <TabsContent value="cancelled" className="mt-6">
          <BookingsList
            bookings={cancelledBookings}
            emptyMessage="No cancelled bookings."
          />
        </TabsContent>
      </Tabs>

      {/* Booking Details Modal */}
      <Dialog open={showDetails} onOpenChange={setShowDetails}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>Booking Details</DialogTitle>
            <DialogDescription>
              Reference: {selectedBooking?.reference}
            </DialogDescription>
          </DialogHeader>
          {selectedBooking && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Flight</p>
                  <p className="font-medium">{selectedBooking.flight.flightNumber}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Airline</p>
                  <p className="font-medium">{selectedBooking.flight.airline}</p>
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
              </div>
              <div className="pt-4 border-t">
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">Total Price</span>
                  <span className="text-xl font-bold">£{selectedBooking.totalPrice.toFixed(2)}</span>
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>

      {/* Cancel Confirmation Modal */}
      <Dialog open={showCancelConfirm} onOpenChange={setShowCancelConfirm}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Cancel Booking</DialogTitle>
            <DialogDescription>
              Are you sure you want to cancel booking {selectedBooking?.reference}?
            </DialogDescription>
          </DialogHeader>
          <div className="p-4 bg-warning/10 rounded-lg border border-warning/20 text-sm">
            <p className="font-medium text-warning">Cancellation Policy</p>
            <p className="text-muted-foreground mt-1">
              A 10% cancellation fee will be charged. The remaining amount will be refunded
              to your original payment method within 5-7 business days.
            </p>
          </div>
          <div className="flex gap-3 justify-end">
            <Button variant="outline" onClick={() => setShowCancelConfirm(false)}>
              Keep Booking
            </Button>
            <Button
              variant="destructive"
              onClick={() => {
                console.log('Cancelling booking:', selectedBooking?.reference);
                setShowCancelConfirm(false);
              }}
            >
              Cancel Booking
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};
