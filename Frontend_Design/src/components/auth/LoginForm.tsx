import React, { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { UserRole } from '@/types';
import { Plane, Loader2, Eye, EyeOff } from 'lucide-react';
import { cn } from '@/lib/utils';

interface LoginFormProps {
  onSwitchToRegister: () => void;
  onForgotPassword: () => void;
}

export const LoginForm: React.FC<LoginFormProps> = ({
  onSwitchToRegister,
  onForgotPassword,
}) => {
  const { login } = useAuth();
  const [role, setRole] = useState<UserRole>('customer');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    const success = await login(email, password, role);
    
    if (!success) {
      setError('Invalid email or password. Please try again.');
    }
    
    setIsLoading(false);
  };

  return (
    <div className="w-full max-w-md mx-auto">
      {/* Logo */}
      <div className="text-center mb-8">
        <div className="w-16 h-16 rounded-2xl gradient-sky mx-auto flex items-center justify-center mb-4 shadow-glow">
          <Plane className="w-8 h-8 text-accent-foreground" />
        </div>
        <h1 className="text-2xl font-bold text-foreground">Welcome to SkyBook</h1>
        <p className="text-muted-foreground mt-1">Flight Booking System</p>
      </div>

      {/* Role Toggle */}
      <div className="flex bg-muted p-1 rounded-xl mb-6">
        <button
          type="button"
          className={cn(
            'flex-1 py-3 px-4 rounded-lg font-medium text-sm transition-all duration-200',
            role === 'customer'
              ? 'bg-card text-foreground shadow-sm'
              : 'text-muted-foreground hover:text-foreground'
          )}
          onClick={() => setRole('customer')}
        >
          Customer
        </button>
        <button
          type="button"
          className={cn(
            'flex-1 py-3 px-4 rounded-lg font-medium text-sm transition-all duration-200',
            role === 'admin'
              ? 'bg-card text-foreground shadow-sm'
              : 'text-muted-foreground hover:text-foreground'
          )}
          onClick={() => setRole('admin')}
        >
          Admin
        </button>
      </div>

      {/* Form */}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="email">Email</Label>
          <Input
            id="email"
            type="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="h-12"
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="password">Password</Label>
          <div className="relative">
            <Input
              id="password"
              type={showPassword ? 'text' : 'password'}
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="h-12 pr-10"
            />
            <button
              type="button"
              className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
            </button>
          </div>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Checkbox
              id="remember"
              checked={rememberMe}
              onCheckedChange={(checked) => setRememberMe(checked as boolean)}
            />
            <Label htmlFor="remember" className="text-sm font-normal cursor-pointer">
              Remember me
            </Label>
          </div>
          <button
            type="button"
            className="text-sm text-accent hover:underline"
            onClick={onForgotPassword}
          >
            Forgot password?
          </button>
        </div>

        {error && (
          <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-sm">
            {error}
          </div>
        )}

        <Button type="submit" variant="sky" className="w-full h-12" disabled={isLoading}>
          {isLoading ? (
            <>
              <Loader2 className="w-4 h-4 animate-spin" />
              Signing in...
            </>
          ) : (
            'Sign In'
          )}
        </Button>
      </form>

      {/* Register Link */}
      {role === 'customer' && (
        <p className="text-center mt-6 text-sm text-muted-foreground">
          Don't have an account?{' '}
          <button
            type="button"
            className="text-accent font-medium hover:underline"
            onClick={onSwitchToRegister}
          >
            Sign up
          </button>
        </p>
      )}

      {/* Demo Credentials */}
      <div className="mt-8 p-4 rounded-xl bg-muted/50 border border-border">
        <p className="text-xs font-medium text-muted-foreground mb-2">Demo Credentials:</p>
        <div className="space-y-1 text-xs text-muted-foreground">
          <p><span className="font-medium">Customer:</span> john@example.com / password123</p>
          <p><span className="font-medium">Admin:</span> admin@flights.com / admin123</p>
        </div>
      </div>
    </div>
  );
};
