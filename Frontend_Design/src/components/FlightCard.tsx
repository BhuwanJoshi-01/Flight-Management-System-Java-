import React from 'react';
import { cn } from '@/lib/utils';
import { Flight } from '@/types';
import { Plane, Clock, Users } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { format } from 'date-fns';

interface FlightCardProps {
  flight: Flight;
  onSelect?: (flight: Flight) => void;
  showBookButton?: boolean;
  compact?: boolean;
}

export const FlightCard: React.FC<FlightCardProps> = ({
  flight,
  onSelect,
  showBookButton = true,
  compact = false,
}) => {
  const availableSeats = flight.capacity - flight.bookedSeats;
  const occupancyPercentage = (flight.bookedSeats / flight.capacity) * 100;

  return (
    <div
      className={cn(
        'flight-card bg-card rounded-xl shadow-soft border border-border/50',
        'transition-all duration-300 hover:shadow-lg hover:border-accent/40',
        compact ? 'p-4' : 'p-6'
      )}
    >
      <div className="flex flex-col lg:flex-row lg:items-center gap-4">
        {/* Airline & Flight Number */}
        <div className={cn('flex items-center gap-3', compact ? 'min-w-[140px]' : 'min-w-[180px]')}>
          <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center">
            <Plane className="w-5 h-5 text-accent" />
          </div>
          <div>
            <p className="font-semibold text-foreground">{flight.flightNumber}</p>
            <p className="text-sm text-muted-foreground">{flight.airline}</p>
          </div>
        </div>

        {/* Route */}
        <div className="flex-1 flex items-center gap-4">
          <div className="text-center">
            <p className="text-xl font-bold text-foreground">{flight.originCode}</p>
            <p className="text-sm text-muted-foreground">{flight.origin}</p>
            <p className="text-sm font-medium text-foreground">
              {format(new Date(flight.departureTime), 'HH:mm')}
            </p>
          </div>

          <div className="flex-1 flex items-center gap-2 px-4">
            <div className="h-px flex-1 bg-border"></div>
            <div className="flex flex-col items-center">
              <Plane className="w-4 h-4 text-accent rotate-90" />
              <span className="text-xs text-muted-foreground mt-1">{flight.duration}</span>
            </div>
            <div className="h-px flex-1 bg-border"></div>
          </div>

          <div className="text-center">
            <p className="text-xl font-bold text-foreground">{flight.destinationCode}</p>
            <p className="text-sm text-muted-foreground">{flight.destination}</p>
            <p className="text-sm font-medium text-foreground">
              {format(new Date(flight.arrivalTime), 'HH:mm')}
            </p>
          </div>
        </div>

        {/* Date */}
        <div className="text-center lg:text-left min-w-[100px]">
          <p className="text-sm text-muted-foreground">
            {format(new Date(flight.departureTime), 'EEE, MMM d')}
          </p>
          <p className="text-sm text-muted-foreground">
            {format(new Date(flight.departureTime), 'yyyy')}
          </p>
        </div>

        {/* Seats & Price */}
        <div className="flex items-center gap-6 lg:gap-4">
          <div className="flex items-center gap-2">
            <Users className="w-4 h-4 text-muted-foreground" />
            <div>
              <p
                className={cn(
                  'text-sm font-medium',
                  availableSeats < 20 ? 'text-warning' : 'text-success'
                )}
              >
                {availableSeats} seats left
              </p>
              <div className="w-20 h-1.5 bg-muted rounded-full overflow-hidden">
                <div
                  className={cn(
                    'h-full rounded-full transition-all',
                    occupancyPercentage > 80 ? 'bg-warning' : 'bg-success'
                  )}
                  style={{ width: `${occupancyPercentage}%` }}
                />
              </div>
            </div>
          </div>

          <div className="text-right min-w-[100px]">
            {flight.dynamicPrice !== flight.basePrice && (
              <p className="text-sm text-muted-foreground line-through">
                £{flight.basePrice}
              </p>
            )}
            <p className="text-2xl font-bold text-foreground">£{flight.dynamicPrice}</p>
          </div>

          {showBookButton && (
            <Button variant="sky" onClick={() => onSelect?.(flight)}>
              Select
            </Button>
          )}
        </div>
      </div>
    </div>
  );
};
