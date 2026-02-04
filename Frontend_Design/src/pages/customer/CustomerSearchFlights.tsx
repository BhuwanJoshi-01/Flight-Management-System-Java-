import React, { useState } from 'react';
import { FlightCard } from '@/components/FlightCard';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { Slider } from '@/components/ui/slider';
import { mockFlights, airports } from '@/data/mockData';
import { Flight } from '@/types';
import { Search, Filter, ArrowRightLeft, Plane, SlidersHorizontal } from 'lucide-react';
import { cn } from '@/lib/utils';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from '@/components/ui/sheet';

interface CustomerSearchFlightsProps {
  onNavigate: (page: string) => void;
}

export const CustomerSearchFlights: React.FC<CustomerSearchFlightsProps> = ({ onNavigate }) => {
  const [tripType, setTripType] = useState<'one-way' | 'round-trip'>('one-way');
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [departureDate, setDepartureDate] = useState('');
  const [passengers, setPassengers] = useState(1);
  const [priceRange, setPriceRange] = useState([0, 1500]);
  const [sortBy, setSortBy] = useState('price-low');
  const [searchResults, setSearchResults] = useState<Flight[]>(mockFlights);
  const [hasSearched, setHasSearched] = useState(false);

  const handleSearch = () => {
    let results = [...mockFlights];

    if (origin) {
      results = results.filter(
        (f) =>
          f.origin.toLowerCase().includes(origin.toLowerCase()) ||
          f.originCode.toLowerCase().includes(origin.toLowerCase())
      );
    }

    if (destination) {
      results = results.filter(
        (f) =>
          f.destination.toLowerCase().includes(destination.toLowerCase()) ||
          f.destinationCode.toLowerCase().includes(destination.toLowerCase())
      );
    }

    results = results.filter(
      (f) => f.dynamicPrice >= priceRange[0] && f.dynamicPrice <= priceRange[1]
    );

    // Sort
    switch (sortBy) {
      case 'price-low':
        results.sort((a, b) => a.dynamicPrice - b.dynamicPrice);
        break;
      case 'price-high':
        results.sort((a, b) => b.dynamicPrice - a.dynamicPrice);
        break;
      case 'duration':
        results.sort((a, b) => a.duration.localeCompare(b.duration));
        break;
      case 'departure':
        results.sort(
          (a, b) => new Date(a.departureTime).getTime() - new Date(b.departureTime).getTime()
        );
        break;
    }

    setSearchResults(results);
    setHasSearched(true);
  };

  const swapLocations = () => {
    const temp = origin;
    setOrigin(destination);
    setDestination(temp);
  };

  const FilterPanel = () => (
    <div className="space-y-6">
      <div>
        <Label className="text-sm font-medium mb-3 block">Price Range</Label>
        <div className="px-2">
          <Slider
            value={priceRange}
            onValueChange={setPriceRange}
            max={1500}
            min={0}
            step={50}
            className="mb-2"
          />
          <div className="flex justify-between text-sm text-muted-foreground">
            <span>£{priceRange[0]}</span>
            <span>£{priceRange[1]}</span>
          </div>
        </div>
      </div>

      <div>
        <Label className="text-sm font-medium mb-3 block">Departure Time</Label>
        <div className="space-y-2">
          {['Morning (6AM - 12PM)', 'Afternoon (12PM - 6PM)', 'Evening (6PM - 12AM)'].map(
            (time) => (
              <div key={time} className="flex items-center gap-2">
                <Checkbox id={time} />
                <Label htmlFor={time} className="text-sm font-normal cursor-pointer">
                  {time}
                </Label>
              </div>
            )
          )}
        </div>
      </div>

      <div>
        <Label className="text-sm font-medium mb-3 block">Airlines</Label>
        <div className="space-y-2">
          {['British Airways', 'Emirates', 'Lufthansa', 'Air France', 'Singapore Airlines'].map(
            (airline) => (
              <div key={airline} className="flex items-center gap-2">
                <Checkbox id={airline} />
                <Label htmlFor={airline} className="text-sm font-normal cursor-pointer">
                  {airline}
                </Label>
              </div>
            )
          )}
        </div>
      </div>

      <Button variant="sky" className="w-full" onClick={handleSearch}>
        Apply Filters
      </Button>
    </div>
  );

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="text-2xl font-bold text-foreground">Search Flights</h1>
        <p className="text-muted-foreground">Find the perfect flight for your journey</p>
      </div>

      {/* Search Form */}
      <div className="bg-card rounded-2xl p-6 shadow-soft border border-border/50">
        {/* Trip Type Toggle */}
        <div className="flex gap-2 mb-6">
          <button
            className={cn(
              'px-4 py-2 rounded-lg font-medium text-sm transition-all',
              tripType === 'one-way'
                ? 'bg-accent text-accent-foreground'
                : 'bg-muted text-muted-foreground hover:bg-muted/80'
            )}
            onClick={() => setTripType('one-way')}
          >
            One Way
          </button>
          <button
            className={cn(
              'px-4 py-2 rounded-lg font-medium text-sm transition-all',
              tripType === 'round-trip'
                ? 'bg-accent text-accent-foreground'
                : 'bg-muted text-muted-foreground hover:bg-muted/80'
            )}
            onClick={() => setTripType('round-trip')}
          >
            Round Trip
          </button>
        </div>

        {/* Search Fields */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
          <div className="lg:col-span-2 flex items-end gap-2">
            <div className="flex-1">
              <Label>From</Label>
              <Select value={origin} onValueChange={setOrigin}>
                <SelectTrigger className="h-12">
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
            <Button
              variant="outline"
              size="icon"
              className="h-12 w-12 shrink-0"
              onClick={swapLocations}
            >
              <ArrowRightLeft className="w-4 h-4" />
            </Button>
            <div className="flex-1">
              <Label>To</Label>
              <Select value={destination} onValueChange={setDestination}>
                <SelectTrigger className="h-12">
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
          </div>

          <div>
            <Label>Departure</Label>
            <Input
              type="date"
              value={departureDate}
              onChange={(e) => setDepartureDate(e.target.value)}
              className="h-12"
            />
          </div>

          <div>
            <Label>Passengers</Label>
            <Select value={passengers.toString()} onValueChange={(v) => setPassengers(parseInt(v))}>
              <SelectTrigger className="h-12">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {[1, 2, 3, 4, 5, 6, 7, 8, 9].map((num) => (
                  <SelectItem key={num} value={num.toString()}>
                    {num} {num === 1 ? 'Passenger' : 'Passengers'}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex items-end">
            <Button variant="sky" className="w-full h-12" onClick={handleSearch}>
              <Search className="w-4 h-4 mr-2" />
              Search
            </Button>
          </div>
        </div>
      </div>

      {/* Results Section */}
      <div className="flex gap-6">
        {/* Filters - Desktop */}
        <div className="hidden lg:block w-72 shrink-0">
          <div className="bg-card rounded-xl p-5 shadow-soft border border-border/50 sticky top-4">
            <h3 className="font-semibold mb-4 flex items-center gap-2">
              <Filter className="w-4 h-4" />
              Filters
            </h3>
            <FilterPanel />
          </div>
        </div>

        {/* Results */}
        <div className="flex-1">
          {/* Results Header */}
          <div className="flex items-center justify-between mb-4">
            <p className="text-muted-foreground">
              {searchResults.length} flights found
            </p>
            <div className="flex items-center gap-3">
              {/* Mobile Filter Button */}
              <Sheet>
                <SheetTrigger asChild>
                  <Button variant="outline" size="sm" className="lg:hidden">
                    <SlidersHorizontal className="w-4 h-4 mr-2" />
                    Filters
                  </Button>
                </SheetTrigger>
                <SheetContent>
                  <SheetHeader>
                    <SheetTitle>Filters</SheetTitle>
                    <SheetDescription>Refine your search results</SheetDescription>
                  </SheetHeader>
                  <div className="mt-6">
                    <FilterPanel />
                  </div>
                </SheetContent>
              </Sheet>

              <Select value={sortBy} onValueChange={setSortBy}>
                <SelectTrigger className="w-48">
                  <SelectValue placeholder="Sort by" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="price-low">Price: Low to High</SelectItem>
                  <SelectItem value="price-high">Price: High to Low</SelectItem>
                  <SelectItem value="duration">Duration</SelectItem>
                  <SelectItem value="departure">Departure Time</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          {/* Flight Cards */}
          {searchResults.length > 0 ? (
            <div className="space-y-4">
              {searchResults.map((flight) => (
                <FlightCard
                  key={flight.id}
                  flight={flight}
                  onSelect={() => {
                    // Handle flight selection - would typically open booking modal
                    console.log('Selected flight:', flight);
                  }}
                />
              ))}
            </div>
          ) : (
            <div className="text-center py-16 bg-card rounded-xl border border-border/50">
              <Plane className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <h3 className="text-lg font-semibold mb-2">No flights found</h3>
              <p className="text-muted-foreground">
                Try adjusting your search criteria or filters
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
