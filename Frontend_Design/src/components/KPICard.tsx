import React from 'react';
import { cn } from '@/lib/utils';
import { LucideIcon } from 'lucide-react';

interface KPICardProps {
  title: string;
  value: string | number;
  icon: LucideIcon;
  trend?: {
    value: number;
    isPositive: boolean;
  };
  accentColor?: 'sky' | 'success' | 'warning' | 'navy';
  className?: string;
}

const accentColors = {
  sky: 'before:bg-accent',
  success: 'before:bg-success',
  warning: 'before:bg-warning',
  navy: 'before:bg-primary',
};

const iconColors = {
  sky: 'bg-accent/10 text-accent',
  success: 'bg-success/10 text-success',
  warning: 'bg-warning/10 text-warning',
  navy: 'bg-primary/10 text-primary',
};

export const KPICard: React.FC<KPICardProps> = ({
  title,
  value,
  icon: Icon,
  trend,
  accentColor = 'sky',
  className,
}) => {
  return (
    <div
      className={cn(
        'kpi-card relative overflow-hidden rounded-xl bg-card p-6 shadow-soft border border-border/50',
        'transition-all duration-300 hover:shadow-md hover:border-accent/30',
        'before:absolute before:top-0 before:left-0 before:w-1 before:h-full',
        accentColors[accentColor],
        className
      )}
    >
      <div className="flex items-start justify-between">
        <div className="space-y-2">
          <p className="text-sm font-medium text-muted-foreground uppercase tracking-wider">
            {title}
          </p>
          <p className="text-3xl font-bold text-foreground">{value}</p>
          {trend && (
            <p
              className={cn(
                'text-sm font-medium flex items-center gap-1',
                trend.isPositive ? 'text-success' : 'text-destructive'
              )}
            >
              <span>{trend.isPositive ? '↑' : '↓'}</span>
              <span>{Math.abs(trend.value)}%</span>
              <span className="text-muted-foreground">vs last month</span>
            </p>
          )}
        </div>
        <div className={cn('p-3 rounded-xl', iconColors[accentColor])}>
          <Icon className="w-6 h-6" />
        </div>
      </div>
    </div>
  );
};
