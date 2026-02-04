import React, { useState } from 'react';
import { mockCustomers, mockBookings } from '@/data/mockData';
import { Customer } from '@/types';
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
import { Plus, Search, Eye, Trash2, User, Award, Download, Mail } from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';

const tierColors = {
  bronze: 'bg-amber-100 text-amber-800 border-amber-200',
  silver: 'bg-gray-100 text-gray-800 border-gray-200',
  gold: 'bg-yellow-100 text-yellow-800 border-yellow-200',
  platinum: 'bg-purple-100 text-purple-800 border-purple-200',
};

export const AdminCustomers: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>(mockCustomers);
  const [searchTerm, setSearchTerm] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  const filteredCustomers = customers.filter(
    (c) =>
      c.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      c.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      c.phone.includes(searchTerm)
  );

  const getCustomerBookings = (customerId: string) => {
    return mockBookings.filter((b) => b.customerId === customerId);
  };

  const handleDelete = (id: string) => {
    if (confirm('Are you sure you want to delete this customer?')) {
      setCustomers(customers.filter((c) => c.id !== id));
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Customers Management</h1>
          <p className="text-muted-foreground">Manage all registered customers</p>
        </div>
        <Dialog open={showAddModal} onOpenChange={setShowAddModal}>
          <DialogTrigger asChild>
            <Button variant="sky">
              <Plus className="w-4 h-4 mr-2" />
              Add Customer
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add New Customer</DialogTitle>
              <DialogDescription>Register a new customer in the system</DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="space-y-2">
                <Label>Full Name</Label>
                <Input placeholder="John Smith" />
              </div>
              <div className="space-y-2">
                <Label>Email Address</Label>
                <Input type="email" placeholder="john@example.com" />
              </div>
              <div className="space-y-2">
                <Label>Phone Number</Label>
                <Input type="tel" placeholder="+44 7700 900123" />
              </div>
            </div>
            <div className="flex justify-end gap-3">
              <Button variant="outline" onClick={() => setShowAddModal(false)}>
                Cancel
              </Button>
              <Button variant="sky" onClick={() => setShowAddModal(false)}>
                Add Customer
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
            placeholder="Search customers..."
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

      {/* Customers Table */}
      <div className="bg-card rounded-xl shadow-soft border border-border/50 overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/30">
              <TableHead>Customer</TableHead>
              <TableHead>Contact</TableHead>
              <TableHead>Tier</TableHead>
              <TableHead>Points</TableHead>
              <TableHead>Bookings</TableHead>
              <TableHead>Member Since</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredCustomers.map((customer) => (
              <TableRow key={customer.id} className="hover:bg-muted/20">
                <TableCell>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-accent/10 flex items-center justify-center">
                      <User className="w-4 h-4 text-accent" />
                    </div>
                    <p className="font-medium">{customer.name}</p>
                  </div>
                </TableCell>
                <TableCell>
                  <p>{customer.email}</p>
                  <p className="text-xs text-muted-foreground">{customer.phone}</p>
                </TableCell>
                <TableCell>
                  <Badge className={cn('border capitalize', tierColors[customer.tier])}>
                    {customer.tier}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className="flex items-center gap-1">
                    <Award className="w-4 h-4 text-warning" />
                    <span className="font-medium">{customer.rewardPoints.toLocaleString()}</span>
                  </div>
                </TableCell>
                <TableCell>
                  <span className="font-medium">{customer.totalBookings}</span>
                </TableCell>
                <TableCell>
                  <p className="text-muted-foreground">
                    {format(new Date(customer.createdAt), 'MMM d, yyyy')}
                  </p>
                </TableCell>
                <TableCell>
                  <div className="flex items-center justify-end gap-1">
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => {
                        setSelectedCustomer(customer);
                        setShowDetailsModal(true);
                      }}
                    >
                      <Eye className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="icon">
                      <Mail className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="text-destructive hover:text-destructive hover:bg-destructive/10"
                      onClick={() => handleDelete(customer.id)}
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

      {/* Customer Details Modal */}
      <Dialog open={showDetailsModal} onOpenChange={setShowDetailsModal}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Customer Details</DialogTitle>
            <DialogDescription>{selectedCustomer?.name}</DialogDescription>
          </DialogHeader>
          {selectedCustomer && (
            <div className="space-y-6">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Email</p>
                  <p className="font-medium">{selectedCustomer.email}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Phone</p>
                  <p className="font-medium">{selectedCustomer.phone}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Member Since</p>
                  <p className="font-medium">
                    {format(new Date(selectedCustomer.createdAt), 'PPP')}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground">Tier</p>
                  <Badge className={cn('border capitalize', tierColors[selectedCustomer.tier])}>
                    {selectedCustomer.tier}
                  </Badge>
                </div>
                <div>
                  <p className="text-muted-foreground">Reward Points</p>
                  <p className="font-medium">{selectedCustomer.rewardPoints.toLocaleString()}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Total Bookings</p>
                  <p className="font-medium">{selectedCustomer.totalBookings}</p>
                </div>
              </div>

              {/* Customer's Bookings */}
              <div>
                <h4 className="font-medium mb-3">Booking History</h4>
                <div className="space-y-2">
                  {getCustomerBookings(selectedCustomer.id).map((booking) => (
                    <div
                      key={booking.id}
                      className="flex items-center justify-between p-3 bg-muted/30 rounded-lg"
                    >
                      <div>
                        <p className="font-medium">{booking.reference}</p>
                        <p className="text-xs text-muted-foreground">
                          {booking.flight.flightNumber} • {booking.flight.originCode} →{' '}
                          {booking.flight.destinationCode}
                        </p>
                      </div>
                      <div className="text-right">
                        <p className="font-medium">£{booking.totalPrice}</p>
                        <Badge
                          variant="secondary"
                          className={cn(
                            'text-xs',
                            booking.status === 'confirmed'
                              ? 'bg-success/10 text-success'
                              : booking.status === 'cancelled'
                              ? 'bg-destructive/10 text-destructive'
                              : ''
                          )}
                        >
                          {booking.status}
                        </Badge>
                      </div>
                    </div>
                  ))}
                  {getCustomerBookings(selectedCustomer.id).length === 0 && (
                    <p className="text-sm text-muted-foreground text-center py-4">
                      No bookings found
                    </p>
                  )}
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};
