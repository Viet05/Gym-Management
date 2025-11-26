import React from 'react';
import { BarChart as BarChartIcon, Users, CreditCard, Activity } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar, PieChart, Pie, Cell } from 'recharts';
import { StatCard } from '../components/StatCard';
import { MOCK_REVENUE_DATA, MOCK_DISTRIBUTION_DATA } from '../constants';

const COLORS = ['#06b6d4', '#3b82f6', '#f97316'];

export const Dashboard: React.FC = () => {
  return (
    <div className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Dashboard</h1>
          <p className="text-slate-400 mt-1">Welcome back, Admin. Here's what's happening today.</p>
        </div>
        <div className="flex space-x-2">
            <span className="px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 text-sm border border-emerald-500/20 animate-pulse">
                System Online
            </span>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard 
          title="Total Revenue" 
          value="$12,450" 
          trend={12.5} 
          icon={<CreditCard className="w-6 h-6" />}
          colorClass="text-emerald-400"
        />
        <StatCard 
          title="Active Members" 
          value="1,240" 
          trend={5.2} 
          icon={<Users className="w-6 h-6" />}
          colorClass="text-gym-400"
        />
        <StatCard 
          title="New Signups" 
          value="48" 
          trend={-2.4} 
          icon={<Activity className="w-6 h-6" />}
          colorClass="text-purple-400"
        />
        <StatCard 
          title="Daily Visits" 
          value="342" 
          trend={8.1} 
          icon={<BarChartIcon className="w-6 h-6" />}
          colorClass="text-orange-400"
        />
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Revenue Chart */}
        <div className="lg:col-span-2 bg-gym-900 border border-gym-800 rounded-xl p-6 shadow-lg">
          <h3 className="text-lg font-bold text-slate-100 mb-6">Revenue Overview</h3>
          <div className="h-80 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={MOCK_REVENUE_DATA}>
                <defs>
                  <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#06b6d4" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#64748b" />
                <YAxis stroke="#64748b" />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: '#1e293b', color: '#f8fafc' }}
                  itemStyle={{ color: '#22d3ee' }}
                />
                <Area type="monotone" dataKey="revenue" stroke="#06b6d4" strokeWidth={3} fillOpacity={1} fill="url(#colorRevenue)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Member Distribution */}
        <div className="bg-gym-900 border border-gym-800 rounded-xl p-6 shadow-lg">
          <h3 className="text-lg font-bold text-slate-100 mb-6">Membership Tier</h3>
          <div className="h-80 w-full relative">
             <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={MOCK_DISTRIBUTION_DATA}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={100}
                  fill="#8884d8"
                  paddingAngle={5}
                  dataKey="value"
                >
                  {MOCK_DISTRIBUTION_DATA.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} stroke="none" />
                  ))}
                </Pie>
                <Tooltip 
                   contentStyle={{ backgroundColor: '#0f172a', borderColor: '#1e293b', borderRadius: '8px' }}
                />
              </PieChart>
            </ResponsiveContainer>
            {/* Custom Legend */}
            <div className="absolute bottom-0 w-full flex justify-center space-x-4">
                {MOCK_DISTRIBUTION_DATA.map((entry, index) => (
                    <div key={entry.name} className="flex items-center text-xs text-slate-400">
                        <span className="w-3 h-3 rounded-full mr-2" style={{ backgroundColor: COLORS[index] }}></span>
                        {entry.name}
                    </div>
                ))}
            </div>
          </div>
        </div>
      </div>
      
      {/* Daily Visits Bar Chart */}
      <div className="bg-gym-900 border border-gym-800 rounded-xl p-6 shadow-lg">
          <h3 className="text-lg font-bold text-slate-100 mb-6">Weekly Gym Traffic</h3>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={MOCK_REVENUE_DATA}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
                <XAxis dataKey="name" stroke="#64748b" />
                <YAxis stroke="#64748b" />
                <Tooltip 
                  cursor={{fill: '#1e293b'}}
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: '#1e293b', color: '#f8fafc' }}
                />
                <Bar dataKey="visits" fill="#f97316" radius={[4, 4, 0, 0]} barSize={40} />
              </BarChart>
            </ResponsiveContainer>
          </div>
      </div>
    </div>
  );
};
