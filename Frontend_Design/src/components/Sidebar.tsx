import React from 'react';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/contexts/AuthContext';
import {
  LayoutDashboard,
  Plane,
  Users,
  CalendarCheck,
  User,
  Bell,
  Search,
  TrendingUp,
  Settings,
  LogOut,
  Shield,
} from 'lucide-react';

interface SidebarProps {
  currentPage: string;
  onNavigate: (page: string) => void;
}

interface NavItem {
  id: string;
  label: string;
  icon: React.ElementType;
}

const customerNavItems: NavItem[] = [
  { id: 'dashboard', label: 'Home', icon: LayoutDashboard },
  { id: 'search', label: 'Search Flights', icon: Search },
  { id: 'bookings', label: 'My Bookings', icon: CalendarCheck },
  { id: 'profile', label: 'Profile', icon: User },
  { id: 'notifications', label: 'Notifications', icon: Bell },
];

const adminNavItems: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { id: 'flights', label: 'Flights', icon: Plane },
  { id: 'customers', label: 'Customers', icon: Users },
  { id: 'bookings', label: 'Bookings', icon: CalendarCheck },
  { id: 'revenue', label: 'Revenue', icon: TrendingUp },
  { id: 'settings', label: 'Settings', icon: Settings },
];

export const Sidebar: React.FC<SidebarProps> = ({ currentPage, onNavigate }) => {
  const { user, logout } = useAuth();
  const isAdmin = user?.role === 'admin';
  const navItems = isAdmin ? adminNavItems : customerNavItems;

  return (
    <aside className="w-64 bg-sidebar min-h-screen flex flex-col">
      {/* Logo */}
      <div className="p-6 border-b border-sidebar-border">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-sidebar-primary flex items-center justify-center">
            <Plane className="w-5 h-5 text-sidebar-primary-foreground" />
          </div>
          <div>
            <h1 className="font-bold text-sidebar-foreground">SkyBook</h1>
            <p className="text-xs text-sidebar-foreground/60">Flight System</p>
          </div>
        </div>
      </div>

      {/* Role Badge */}
      <div className="px-6 py-4">
        <div className={cn(
          'flex items-center gap-2 px-3 py-2 rounded-lg text-sm',
          isAdmin ? 'bg-warning/20 text-warning' : 'bg-accent/20 text-accent'
        )}>
          <Shield className="w-4 h-4" />
          <span className="font-medium">{isAdmin ? 'Admin Portal' : 'Customer Portal'}</span>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 px-4 py-2 space-y-1">
        {navItems.map((item) => (
          <Button
            key={item.id}
            variant={currentPage === item.id ? 'nav-active' : 'nav'}
            className="w-full justify-start gap-3"
            onClick={() => onNavigate(item.id)}
          >
            <item.icon className="w-5 h-5" />
            {item.label}
          </Button>
        ))}
      </nav>

      {/* User & Logout */}
      <div className="p-4 border-t border-sidebar-border space-y-3">
        <div className="flex items-center gap-3 px-3 py-2">
          <div className="w-9 h-9 rounded-full bg-sidebar-accent flex items-center justify-center">
            <User className="w-4 h-4 text-sidebar-foreground" />
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-sidebar-foreground truncate">
              {user?.name}
            </p>
            <p className="text-xs text-sidebar-foreground/60 truncate">{user?.email}</p>
          </div>
        </div>
        <Button
          variant="ghost"
          className="w-full justify-start gap-3 text-sidebar-foreground/70 hover:text-sidebar-foreground hover:bg-sidebar-accent"
          onClick={logout}
        >
          <LogOut className="w-5 h-5" />
          Sign Out
        </Button>
      </div>
    </aside>
  );
};
