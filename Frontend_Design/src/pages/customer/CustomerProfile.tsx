import React, { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Switch } from '@/components/ui/switch';
import { User, Lock, Settings, Award, CreditCard, Save, Loader2 } from 'lucide-react';
import { cn } from '@/lib/utils';

export const CustomerProfile: React.FC = () => {
  const { user } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [profileData, setProfileData] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phone: user?.phone || '+44 7700 900123',
    dateOfBirth: '1990-05-15',
    gender: 'male',
    preferredSeat: 'window',
    mealPreference: 'standard',
    emailNotifications: true,
    smsNotifications: false,
  });

  const handleSave = async () => {
    setIsLoading(true);
    await new Promise((resolve) => setTimeout(resolve, 1000));
    setIsLoading(false);
    console.log('Profile saved:', profileData);
  };

  const tierColors = {
    bronze: 'from-amber-600 to-amber-800',
    silver: 'from-gray-400 to-gray-600',
    gold: 'from-yellow-400 to-yellow-600',
    platinum: 'from-purple-400 to-purple-600',
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="text-2xl font-bold text-foreground">My Profile</h1>
        <p className="text-muted-foreground">Manage your account settings and preferences</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Profile Card */}
        <Card className="lg:col-span-1">
          <CardContent className="pt-6">
            <div className="flex flex-col items-center text-center">
              <div className="w-24 h-24 rounded-full bg-accent/10 flex items-center justify-center mb-4">
                <User className="w-12 h-12 text-accent" />
              </div>
              <h3 className="text-lg font-semibold">{user?.name}</h3>
              <p className="text-sm text-muted-foreground">{user?.email}</p>
              
              {/* Rewards Card */}
              <div className={cn(
                'w-full mt-6 p-4 rounded-xl bg-gradient-to-br text-white',
                tierColors.gold
              )}>
                <div className="flex items-center justify-between mb-3">
                  <Award className="w-6 h-6" />
                  <span className="text-xs uppercase tracking-wider opacity-80">Gold Member</span>
                </div>
                <div className="text-left">
                  <p className="text-2xl font-bold">2,500</p>
                  <p className="text-sm opacity-80">Reward Points</p>
                </div>
              </div>

              <div className="w-full mt-4 p-4 bg-muted/50 rounded-xl text-left">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Next tier</span>
                  <span className="font-medium">Platinum</span>
                </div>
                <div className="mt-2 h-2 bg-muted rounded-full overflow-hidden">
                  <div className="h-full w-3/4 bg-accent rounded-full" />
                </div>
                <p className="text-xs text-muted-foreground mt-1">500 points to go</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Settings Tabs */}
        <Card className="lg:col-span-2">
          <Tabs defaultValue="personal" className="w-full">
            <CardHeader>
              <TabsList className="grid w-full grid-cols-4">
                <TabsTrigger value="personal" className="gap-2">
                  <User className="w-4 h-4" />
                  Personal
                </TabsTrigger>
                <TabsTrigger value="security" className="gap-2">
                  <Lock className="w-4 h-4" />
                  Security
                </TabsTrigger>
                <TabsTrigger value="preferences" className="gap-2">
                  <Settings className="w-4 h-4" />
                  Preferences
                </TabsTrigger>
                <TabsTrigger value="payment" className="gap-2">
                  <CreditCard className="w-4 h-4" />
                  Payment
                </TabsTrigger>
              </TabsList>
            </CardHeader>

            <CardContent>
              <TabsContent value="personal" className="space-y-4 mt-0">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label>Full Name</Label>
                    <Input
                      value={profileData.name}
                      onChange={(e) => setProfileData({ ...profileData, name: e.target.value })}
                    />
                  </div>
                  <div className="space-y-2">
                    <Label>Email (read-only)</Label>
                    <Input value={profileData.email} disabled className="bg-muted" />
                  </div>
                  <div className="space-y-2">
                    <Label>Phone Number</Label>
                    <Input
                      value={profileData.phone}
                      onChange={(e) => setProfileData({ ...profileData, phone: e.target.value })}
                    />
                  </div>
                  <div className="space-y-2">
                    <Label>Date of Birth</Label>
                    <Input
                      type="date"
                      value={profileData.dateOfBirth}
                      onChange={(e) => setProfileData({ ...profileData, dateOfBirth: e.target.value })}
                    />
                  </div>
                  <div className="space-y-2">
                    <Label>Gender</Label>
                    <Select
                      value={profileData.gender}
                      onValueChange={(value) => setProfileData({ ...profileData, gender: value })}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="male">Male</SelectItem>
                        <SelectItem value="female">Female</SelectItem>
                        <SelectItem value="other">Other</SelectItem>
                        <SelectItem value="prefer-not">Prefer not to say</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
                <Button variant="sky" onClick={handleSave} disabled={isLoading}>
                  {isLoading ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin mr-2" />
                      Saving...
                    </>
                  ) : (
                    <>
                      <Save className="w-4 h-4 mr-2" />
                      Save Changes
                    </>
                  )}
                </Button>
              </TabsContent>

              <TabsContent value="security" className="space-y-4 mt-0">
                <div className="space-y-4">
                  <div className="space-y-2">
                    <Label>Current Password</Label>
                    <Input type="password" placeholder="Enter current password" />
                  </div>
                  <div className="space-y-2">
                    <Label>New Password</Label>
                    <Input type="password" placeholder="Enter new password" />
                  </div>
                  <div className="space-y-2">
                    <Label>Confirm New Password</Label>
                    <Input type="password" placeholder="Confirm new password" />
                  </div>
                </div>
                <Button variant="sky">Update Password</Button>
              </TabsContent>

              <TabsContent value="preferences" className="space-y-6 mt-0">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label>Preferred Seat</Label>
                    <Select
                      value={profileData.preferredSeat}
                      onValueChange={(value) => setProfileData({ ...profileData, preferredSeat: value })}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="window">Window</SelectItem>
                        <SelectItem value="aisle">Aisle</SelectItem>
                        <SelectItem value="middle">Middle</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <Label>Meal Preference</Label>
                    <Select
                      value={profileData.mealPreference}
                      onValueChange={(value) => setProfileData({ ...profileData, mealPreference: value })}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="standard">Standard</SelectItem>
                        <SelectItem value="vegetarian">Vegetarian</SelectItem>
                        <SelectItem value="vegan">Vegan</SelectItem>
                        <SelectItem value="halal">Halal</SelectItem>
                        <SelectItem value="kosher">Kosher</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                <div className="space-y-4">
                  <h4 className="font-medium">Notification Settings</h4>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Email Notifications</p>
                      <p className="text-sm text-muted-foreground">
                        Receive booking confirmations and updates via email
                      </p>
                    </div>
                    <Switch
                      checked={profileData.emailNotifications}
                      onCheckedChange={(checked) =>
                        setProfileData({ ...profileData, emailNotifications: checked })
                      }
                    />
                  </div>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">SMS Notifications</p>
                      <p className="text-sm text-muted-foreground">
                        Get flight updates and alerts via SMS
                      </p>
                    </div>
                    <Switch
                      checked={profileData.smsNotifications}
                      onCheckedChange={(checked) =>
                        setProfileData({ ...profileData, smsNotifications: checked })
                      }
                    />
                  </div>
                </div>
                <Button variant="sky" onClick={handleSave}>Save Preferences</Button>
              </TabsContent>

              <TabsContent value="payment" className="mt-0">
                <div className="text-center py-8">
                  <CreditCard className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
                  <h3 className="font-semibold mb-2">No payment methods saved</h3>
                  <p className="text-sm text-muted-foreground mb-4">
                    Add a payment method for faster checkout
                  </p>
                  <Button variant="outline">Add Payment Method</Button>
                </div>
              </TabsContent>
            </CardContent>
          </Tabs>
        </Card>
      </div>
    </div>
  );
};
