import React, { useState } from 'react';
import { LoginForm } from '@/components/auth/LoginForm';
import { RegisterForm } from '@/components/auth/RegisterForm';

type AuthView = 'login' | 'register' | 'forgot';

export const AuthPage: React.FC = () => {
  const [view, setView] = useState<AuthView>('login');

  return (
    <div className="min-h-screen flex">
      {/* Left Side - Form */}
      <div className="flex-1 flex items-center justify-center p-8 bg-card">
        <div className="w-full max-w-md animate-fade-in">
          {view === 'login' && (
            <LoginForm
              onSwitchToRegister={() => setView('register')}
              onForgotPassword={() => setView('forgot')}
            />
          )}
          {view === 'register' && (
            <RegisterForm onSwitchToLogin={() => setView('login')} />
          )}
          {view === 'forgot' && (
            <div className="text-center">
              <h2 className="text-2xl font-bold mb-4">Reset Password</h2>
              <p className="text-muted-foreground mb-6">
                Enter your email address and we'll send you a link to reset your password.
              </p>
              <input
                type="email"
                placeholder="Enter your email"
                className="w-full h-12 px-4 rounded-lg border border-input bg-background mb-4"
              />
              <button className="w-full h-12 rounded-lg bg-accent text-accent-foreground font-semibold mb-4">
                Send Reset Link
              </button>
              <button
                onClick={() => setView('login')}
                className="text-sm text-accent hover:underline"
              >
                Back to login
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Right Side - Hero Image */}
      <div className="hidden lg:flex flex-1 gradient-navy relative overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 right-20 w-96 h-96 rounded-full bg-accent blur-3xl" />
          <div className="absolute bottom-20 left-20 w-72 h-72 rounded-full bg-sky blur-3xl" />
        </div>
        
        <div className="relative z-10 flex flex-col items-center justify-center p-12 text-center">
          <div className="max-w-lg space-y-6">
            <h2 className="text-4xl font-bold text-primary-foreground">
              Your Journey Starts Here
            </h2>
            <p className="text-lg text-primary-foreground/80">
              Book flights to destinations around the world with our modern, easy-to-use booking system.
            </p>
            
            <div className="grid grid-cols-3 gap-6 pt-8">
              <div className="text-center">
                <p className="text-3xl font-bold text-accent">500+</p>
                <p className="text-sm text-primary-foreground/60">Destinations</p>
              </div>
              <div className="text-center">
                <p className="text-3xl font-bold text-accent">50K+</p>
                <p className="text-sm text-primary-foreground/60">Happy Travelers</p>
              </div>
              <div className="text-center">
                <p className="text-3xl font-bold text-accent">24/7</p>
                <p className="text-sm text-primary-foreground/60">Support</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
