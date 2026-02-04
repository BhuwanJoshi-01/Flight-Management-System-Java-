import React from 'react';
import { cn } from '@/lib/utils';
import { Booking } from '@/types';
import { Plane, Calendar, Users, Download, Eye, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { format } from 'date-fns';

interface BookingCardProps {
  booking: Booking;
  onView?: (booking: Booking) => void;
  onCancel?: (booking: Booking) => void;
  onDownload?: (booking: Booking) => void;
  isAdmin?: boolean;
}

const statusStyles = {
  confirmed: 'bg-success/10 text-success border-success/20',
  pending: 'bg-warning/10 text-warning border-warning/20',
  cancelled: 'bg-destructive/10 text-destructive border-destructive/20',
  completed: 'bg-muted text-muted-foreground border-border',
};

const statusLabels = {
  confirmed: 'Confirmed',
  pending: 'Pending',
  cancelled: 'Cancelled',
  completed: 'Completed',
};

export const BookingCard: React.FC<BookingCardProps> = ({
  booking,
  onView,
  onCancel,
  onDownload,
  isAdmin = false,
}) => {
  const flight = booking.flight;

  return (
    <div className="bg-card rounded-xl shadow-soft border border-border/50 overflow-hidden transition-all duration-300 hover:shadow-md">
      {/* Header */}
      <div className="flex items-center justify-between px-5 py-3 bg-muted/30 border-b border-border/50">
        <div className="flex items-center gap-3">
          <span className="font-semibold text-foreground">{booking.reference}</span>
          {isAdmin && (
            <span className="text-sm text-muted-foreground">• {booking.customerName}</span>
          )}
        </div>
        <Badge className={cn('border', statusStyles[booking.status])}>
          {statusLabels[booking.status]}
        </Badge>
      </div>

      {/* Content */}
      <div className="p-5">
        <div className="flex flex-col md:flex-row md:items-center gap-4">
          {/* Flight Info */}
          <div className="flex items-center gap-4 flex-1">
            <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center">
              <Plane className="w-5 h-5 text-accent" />
            </div>
            <div>
              <p className="font-semibold text-foreground">{flight.flightNumber}</p>
              <p className="text-sm text-muted-foreground">
                {flight.originCode} → {flight.destinationCode}
              </p>
            </div>
          </div>

          {/* Date & Time */}
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <Calendar className="w-4 h-4" />
            <span>{format(new Date(flight.departureTime), 'EEE, MMM d, yyyy • HH:mm')}</span>
          </div>

          {/* Passengers */}
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <Users className="w-4 h-4" />
            <span>{booking.passengers} {booking.passengers === 1 ? 'passenger' : 'passengers'}</span>
          </div>

          {/* Price */}
          <div className="text-right">
            <p className="text-xl font-bold text-foreground">£{booking.totalPrice.toFixed(2)}</p>
          </div>
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-2 px-5 py-3 bg-muted/20 border-t border-border/50">
        <Button variant="ghost" size="sm" onClick={() => onView?.(booking)}>
          <Eye className="w-4 h-4 mr-1" />
          View Details
        </Button>
        {booking.status === 'confirmed' && (
          <>
            <Button variant="ghost" size="sm" onClick={() => onDownload?.(booking)}>
              <Download className="w-4 h-4 mr-1" />
              E-Ticket
            </Button>
            <Button
              variant="ghost"
              size="sm"
              className="text-destructive hover:text-destructive hover:bg-destructive/10"
              onClick={() => onCancel?.(booking)}
            >
              <X className="w-4 h-4 mr-1" />
              Cancel
            </Button>
          </>
        )}
      </div>
    </div>
  );
};
