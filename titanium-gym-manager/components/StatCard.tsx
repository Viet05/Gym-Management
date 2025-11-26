import React from 'react';
import { ArrowUpRight, ArrowDownRight } from 'lucide-react';

interface StatCardProps {
  title: string;
  value: string | number;
  trend?: number;
  icon: React.ReactNode;
  trendLabel?: string;
  colorClass?: string;
}

export const StatCard: React.FC<StatCardProps> = ({ title, value, trend, icon, trendLabel = "vs last month", colorClass = "text-gym-400" }) => {
  const isPositive = trend && trend >= 0;

  return (
    <div className="bg-gym-900 border border-gym-800 rounded-xl p-6 relative overflow-hidden group hover:border-gym-500/30 transition-all duration-300">
      <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity transform group-hover:scale-110 duration-500">
        {/* Fix: Ensure icon is a valid element and cast types to allow className prop injection */}
        {React.isValidElement(icon) 
          ? React.cloneElement(icon as React.ReactElement<{ className?: string }>, { className: `w-16 h-16 ${colorClass}` })
          : icon
        }
      </div>
      
      <div className="flex items-start justify-between mb-4 relative z-10">
        <div className={`p-3 rounded-lg bg-gym-800/50 ${colorClass}`}>
          {icon}
        </div>
        {trend !== undefined && (
          <div className={`flex items-center text-sm font-medium ${isPositive ? 'text-emerald-400' : 'text-red-400'}`}>
            {isPositive ? <ArrowUpRight className="w-4 h-4 mr-1" /> : <ArrowDownRight className="w-4 h-4 mr-1" />}
            {Math.abs(trend)}%
          </div>
        )}
      </div>
      
      <div className="relative z-10">
        <h3 className="text-slate-400 text-sm font-medium mb-1">{title}</h3>
        <div className="text-2xl font-bold text-slate-100">{value}</div>
        {trend !== undefined && <p className="text-xs text-slate-500 mt-2">{trendLabel}</p>}
      </div>
    </div>
  );
};