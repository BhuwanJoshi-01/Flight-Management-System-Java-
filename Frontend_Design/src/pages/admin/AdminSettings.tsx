import React, { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Slider } from '@/components/ui/slider';
import { Settings, DollarSign, Bell, Database, Save, Loader2 } from 'lucide-react';

export const AdminSettings: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [settings, setSettings] = useState({
    // General
    systemName: 'SkyBook Flight System',
    currency: 'GBP',
    timezone: 'Europe/London',
    dateFormat: 'DD/MM/YYYY',
    // Pricing
    dynamicPricingEnabled: true,
    timeSensitivity: 0.15,
    capacitySensitivity: 0.2,
    maxBookingWindow: 365,
    cancellationFee: 10,
    rebookingFee: 15,
    // Booking
    maxPassengers: 9,
    advanceBookingLimit: 365,
    minBookingNotice: 2,
    autoCancelHours: 24,
    // Notifications
    emailNotifications: true,
    smsNotifications: false,
  });

  const handleSave = async () => {
    setIsLoading(true);
    await new Promise((resolve) => setTimeout(resolve, 1000));
    setIsLoading(false);
    console.log('Settings saved:', settings);
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h1 className="text-2xl font-bold text-foreground">System Settings</h1>
        <p className="text-muted-foreground">Configure system-wide settings and preferences</p>
      </div>

      <Tabs defaultValue="general" className="space-y-6">
        <TabsList className="grid w-full max-w-xl grid-cols-4">
          <TabsTrigger value="general" className="gap-2">
            <Settings className="w-4 h-4" />
            General
          </TabsTrigger>
          <TabsTrigger value="pricing" className="gap-2">
            <DollarSign className="w-4 h-4" />
            Pricing
          </TabsTrigger>
          <TabsTrigger value="notifications" className="gap-2">
            <Bell className="w-4 h-4" />
            Notifications
          </TabsTrigger>
          <TabsTrigger value="data" className="gap-2">
            <Database className="w-4 h-4" />
            Data
          </TabsTrigger>
        </TabsList>

        {/* General Settings */}
        <TabsContent value="general">
          <Card>
            <CardHeader>
              <CardTitle>General Settings</CardTitle>
              <CardDescription>Configure basic system settings</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-2 gap-6">
                <div className="space-y-2">
                  <Label>System Name</Label>
                  <Input
                    value={settings.systemName}
                    onChange={(e) => setSettings({ ...settings, systemName: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <Label>Default Currency</Label>
                  <Select
                    value={settings.currency}
                    onValueChange={(value) => setSettings({ ...settings, currency: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="GBP">GBP (£)</SelectItem>
                      <SelectItem value="USD">USD ($)</SelectItem>
                      <SelectItem value="EUR">EUR (€)</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label>Timezone</Label>
                  <Select
                    value={settings.timezone}
                    onValueChange={(value) => setSettings({ ...settings, timezone: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Europe/London">Europe/London (GMT)</SelectItem>
                      <SelectItem value="America/New_York">America/New York (EST)</SelectItem>
                      <SelectItem value="Asia/Tokyo">Asia/Tokyo (JST)</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label>Date Format</Label>
                  <Select
                    value={settings.dateFormat}
                    onValueChange={(value) => setSettings({ ...settings, dateFormat: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="DD/MM/YYYY">DD/MM/YYYY</SelectItem>
                      <SelectItem value="MM/DD/YYYY">MM/DD/YYYY</SelectItem>
                      <SelectItem value="YYYY-MM-DD">YYYY-MM-DD</SelectItem>
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
            </CardContent>
          </Card>
        </TabsContent>

        {/* Pricing Settings */}
        <TabsContent value="pricing">
          <Card>
            <CardHeader>
              <CardTitle>Pricing Settings</CardTitle>
              <CardDescription>Configure dynamic pricing and fees</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between p-4 bg-muted/50 rounded-lg">
                <div>
                  <p className="font-medium">Dynamic Pricing</p>
                  <p className="text-sm text-muted-foreground">
                    Automatically adjust prices based on demand and time
                  </p>
                </div>
                <Switch
                  checked={settings.dynamicPricingEnabled}
                  onCheckedChange={(checked) =>
                    setSettings({ ...settings, dynamicPricingEnabled: checked })
                  }
                />
              </div>

              {settings.dynamicPricingEnabled && (
                <div className="grid grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <Label>Time Sensitivity Factor (α): {settings.timeSensitivity}</Label>
                    <Slider
                      value={[settings.timeSensitivity * 100]}
                      onValueChange={([value]) =>
                        setSettings({ ...settings, timeSensitivity: value / 100 })
                      }
                      max={50}
                      step={1}
                    />
                    <p className="text-xs text-muted-foreground">
                      Higher values increase price as departure date approaches
                    </p>
                  </div>
                  <div className="space-y-4">
                    <Label>Capacity Sensitivity Factor (β): {settings.capacitySensitivity}</Label>
                    <Slider
                      value={[settings.capacitySensitivity * 100]}
                      onValueChange={([value]) =>
                        setSettings({ ...settings, capacitySensitivity: value / 100 })
                      }
                      max={50}
                      step={1}
                    />
                    <p className="text-xs text-muted-foreground">
                      Higher values increase price as seats fill up
                    </p>
                  </div>
                </div>
              )}

              <div className="grid grid-cols-2 gap-6">
                <div className="space-y-2">
                  <Label>Cancellation Fee (%)</Label>
                  <Input
                    type="number"
                    value={settings.cancellationFee}
                    onChange={(e) =>
                      setSettings({ ...settings, cancellationFee: parseInt(e.target.value) })
                    }
                  />
                </div>
                <div className="space-y-2">
                  <Label>Rebooking Fee (%)</Label>
                  <Input
                    type="number"
                    value={settings.rebookingFee}
                    onChange={(e) =>
                      setSettings({ ...settings, rebookingFee: parseInt(e.target.value) })
                    }
                  />
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
            </CardContent>
          </Card>
        </TabsContent>

        {/* Notification Settings */}
        <TabsContent value="notifications">
          <Card>
            <CardHeader>
              <CardTitle>Notification Settings</CardTitle>
              <CardDescription>Configure system notifications</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between p-4 bg-muted/50 rounded-lg">
                  <div>
                    <p className="font-medium">Email Notifications</p>
                    <p className="text-sm text-muted-foreground">
                      Send booking confirmations and updates via email
                    </p>
                  </div>
                  <Switch
                    checked={settings.emailNotifications}
                    onCheckedChange={(checked) =>
                      setSettings({ ...settings, emailNotifications: checked })
                    }
                  />
                </div>

                <div className="flex items-center justify-between p-4 bg-muted/50 rounded-lg">
                  <div>
                    <p className="font-medium">SMS Notifications</p>
                    <p className="text-sm text-muted-foreground">
                      Send flight alerts and updates via SMS
                    </p>
                  </div>
                  <Switch
                    checked={settings.smsNotifications}
                    onCheckedChange={(checked) =>
                      setSettings({ ...settings, smsNotifications: checked })
                    }
                  />
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
            </CardContent>
          </Card>
        </TabsContent>

        {/* Data Management */}
        <TabsContent value="data">
          <Card>
            <CardHeader>
              <CardTitle>Data Management</CardTitle>
              <CardDescription>Backup, restore, and manage system data</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <Button variant="outline" className="h-24 flex-col gap-2">
                  <Database className="w-6 h-6" />
                  <span>Backup Database</span>
                </Button>
                <Button variant="outline" className="h-24 flex-col gap-2">
                  <Database className="w-6 h-6" />
                  <span>Restore Database</span>
                </Button>
                <Button variant="outline" className="h-24 flex-col gap-2 text-destructive hover:text-destructive">
                  <Database className="w-6 h-6" />
                  <span>Clear Old Data</span>
                </Button>
              </div>
              <div className="p-4 bg-warning/10 rounded-lg border border-warning/20">
                <p className="text-sm text-warning font-medium">Warning</p>
                <p className="text-sm text-muted-foreground mt-1">
                  Data operations can affect system performance. It's recommended to perform these
                  operations during off-peak hours.
                </p>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};
