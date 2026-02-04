import React, { useState } from 'react';
import { mockFlights } from '@/data/mockData';
import { Flight } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
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
  DialogTrigger,
} from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { 
  Plus, 
  Search, 
  Eye, 
  Users, 
  Trash2, 
  Plane,
  Edit,
  Download
} from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';
import { airports } from '@/data/mockData';

export const AdminFlights: React.FC = () => {
  const [flights, setFlights] = useState<Flight[]>(mockFlights);
  const [searchTerm, setSearchTerm] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedFlight, setSelectedFlight] = useState<Flight | null>(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  const filteredFlights = flights.filter(
    (f) =>
      f.flightNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      f.origin.toLowerCase().includes(searchTerm.toLowerCase()) ||
      f.destination.toLowerCase().includes(searchTerm.toLowerCase()) ||
      f.airline.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getOccupancyColor = (booked: number, capacity: number) => {
    const percentage = (booked / capacity) * 100;
    if (percentage >= 80) return 'text-success';
    if (percentage >= 50) return 'text-warning';
    return 'text-destructive';
  };

  const handleDelete = (id: string) => {
    if (confirm('Are you sure you want to delete this flight?')) {
      setFlights(flights.filter((f) => f.id !== id));
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Flights Management</h1>
          <p className="text-muted-foreground">Manage all flights in the system</p>
        </div>
        <Dialog open={showAddModal} onOpenChange={setShowAddModal}>
          <DialogTrigger asChild>
            <Button variant="sky">
              <Plus className="w-4 h-4 mr-2" />
              Add Flight
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Add New Flight</DialogTitle>
              <DialogDescription>Create a new flight in the system</DialogDescription>
            </DialogHeader>
            <div className="grid grid-cols-2 gap-4 py-4">
              <div className="space-y-2">
                <Label>Flight Number</Label>
                <Input placeholder="BA123" />
              </div>
              <div className="space-y-2">
                <Label>Airline</Label>
                <Input placeholder="British Airways" />
              </div>
              <div className="space-y-2">
                <Label>Origin</Label>
                <Select>
                  <SelectTrigger>
                    <SelectValue placeholder="Select origin" />
                  </SelectTrigger>
                  <SelectContent>
                    {airports.map((airport) => (
                      <SelectItem key={airport.code} value={airport.code}>
                        {airport.city} ({airport.code})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Destination</Label>
                <Select>
                  <SelectTrigger>
                    <SelectValue placeholder="Select destination" />
                  </SelectTrigger>
                  <SelectContent>
                    {airports.map((airport) => (
                      <SelectItem key={airport.code} value={airport.code}>
                        {airport.city} ({airport.code})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Departure Date & Time</Label>
                <Input type="datetime-local" />
              </div>
              <div className="space-y-2">
                <Label>Arrival Date & Time</Label>
                <Input type="datetime-local" />
              </div>
              <div className="space-y-2">
                <Label>Capacity</Label>
                <Input type="number" placeholder="180" />
              </div>
              <div className="space-y-2">
                <Label>Base Price (£)</Label>
                <Input type="number" placeholder="450" />
              </div>
            </div>
            <div className="flex justify-end gap-3">
              <Button variant="outline" onClick={() => setShowAddModal(false)}>
                Cancel
              </Button>
              <Button variant="sky" onClick={() => setShowAddModal(false)}>
                Add Flight
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            placeholder="Search flights..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
        <Button variant="outline">
          <Download className="w-4 h-4 mr-2" />
          Export
        </Button>
      </div>

      {/* Flights Table */}
      <div className="bg-card rounded-xl shadow-soft border border-border/50 overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/30">
              <TableHead>Flight</TableHead>
              <TableHead>Route</TableHead>
              <TableHead>Date & Time</TableHead>
              <TableHead>Capacity</TableHead>
              <TableHead>Price</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredFlights.map((flight) => (
              <TableRow key={flight.id} className="hover:bg-muted/20">
                <TableCell>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-accent/10 flex items-center justify-center">
                      <Plane className="w-4 h-4 text-accent" />
                    </div>
                    <div>
                      <p className="font-medium">{flight.flightNumber}</p>
                      <p className="text-xs text-muted-foreground">{flight.airline}</p>
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <span className="font-medium">{flight.originCode}</span>
                  <span className="text-muted-foreground mx-2">→</span>
                  <span className="font-medium">{flight.destinationCode}</span>
                </TableCell>
                <TableCell>
                  <p>{format(new Date(flight.departureTime), 'MMM d, yyyy')}</p>
                  <p className="text-xs text-muted-foreground">
                    {format(new Date(flight.departureTime), 'HH:mm')} -{' '}
                    {format(new Date(flight.arrivalTime), 'HH:mm')}
                  </p>
                </TableCell>
                <TableCell>
                  <div className="flex items-center gap-2">
                    <span className={cn('font-medium', getOccupancyColor(flight.bookedSeats, flight.capacity))}>
                      {flight.bookedSeats}
                    </span>
                    <span className="text-muted-foreground">/ {flight.capacity}</span>
                  </div>
                  <div className="w-20 h-1.5 bg-muted rounded-full mt-1 overflow-hidden">
                    <div
                      className={cn(
                        'h-full rounded-full',
                        (flight.bookedSeats / flight.capacity) * 100 >= 80
                          ? 'bg-success'
                          : (flight.bookedSeats / flight.capacity) * 100 >= 50
                          ? 'bg-warning'
                          : 'bg-destructive'
                      )}
                      style={{ width: `${(flight.bookedSeats / flight.capacity) * 100}%` }}
                    />
                  </div>
                </TableCell>
                <TableCell>
                  <p className="font-medium">£{flight.dynamicPrice}</p>
                  {flight.dynamicPrice !== flight.basePrice && (
                    <p className="text-xs text-muted-foreground line-through">
                      £{flight.basePrice}
                    </p>
                  )}
                </TableCell>
                <TableCell>
                  <Badge
                    className={cn(
                      'border',
                      flight.status === 'scheduled'
                        ? 'bg-success/10 text-success border-success/20'
                        : flight.status === 'delayed'
                        ? 'bg-warning/10 text-warning border-warning/20'
                        : 'bg-destructive/10 text-destructive border-destructive/20'
                    )}
                  >
                    {flight.status}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className="flex items-center justify-end gap-1">
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => {
                        setSelectedFlight(flight);
                        setShowDetailsModal(true);
                      }}
                    >
                      <Eye className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="icon">
                      <Users className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="icon">
                      <Edit className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="text-destructive hover:text-destructive hover:bg-destructive/10"
                      onClick={() => handleDelete(flight.id)}
                    >
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* Flight Details Modal */}
      <Dialog open={showDetailsModal} onOpenChange={setShowDetailsModal}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>Flight Details</DialogTitle>
            <DialogDescription>
              {selectedFlight?.flightNumber} - {selectedFlight?.airline}
            </DialogDescription>
          </DialogHeader>
          {selectedFlight && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Route</p>
                  <p className="font-medium">
                    {selectedFlight.origin} ({selectedFlight.originCode}) →{' '}
                    {selectedFlight.destination} ({selectedFlight.destinationCode})
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Duration</p>
                  <p className="font-medium">{selectedFlight.duration}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Departure</p>
                  <p className="font-medium">
                    {format(new Date(selectedFlight.departureTime), 'PPp')}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Arrival</p>
                  <p className="font-medium">
                    {format(new Date(selectedFlight.arrivalTime), 'PPp')}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Capacity</p>
                  <p className="font-medium">
                    {selectedFlight.bookedSeats} / {selectedFlight.capacity} seats
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Price</p>
                  <p className="font-medium">
                    £{selectedFlight.dynamicPrice}{' '}
                    {selectedFlight.dynamicPrice !== selectedFlight.basePrice && (
                      <span className="text-muted-foreground line-through text-xs">
                        £{selectedFlight.basePrice}
                      </span>
                    )}
                  </p>
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};
