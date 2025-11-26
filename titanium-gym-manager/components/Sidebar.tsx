import React from 'react';
import { LayoutDashboard, Users, Package, LogOut, Dumbbell, CalendarDays } from 'lucide-react';
import { ViewState } from '../types';

interface SidebarProps {
  currentView: ViewState;
  onNavigate: (view: ViewState) => void;
  onLogout: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({ currentView, onNavigate, onLogout }) => {
  const menuItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'calendar', label: 'Calendar', icon: CalendarDays },
    { id: 'members', label: 'Members', icon: Users },
    { id: 'packages', label: 'Packages', icon: Package },
  ];

  return (
    <aside className="w-64 bg-gym-900 border-r border-gym-800 flex flex-col h-screen fixed left-0 top-0 z-20 shadow-2xl">
      <div className="h-20 flex items-center px-8 border-b border-gym-800">
        <Dumbbell className="w-8 h-8 text-gym-500 mr-3" />
        <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-white to-gray-400 tracking-wider">
          TITANIUM
        </span>
      </div>

      <nav className="flex-1 py-6 px-4 space-y-2">
        {menuItems.map((item) => {
          const Icon = item.icon;
          const isActive = currentView === item.id;
          return (
            <button
              key={item.id}
              onClick={() => onNavigate(item.id as ViewState)}
              className={`w-full flex items-center px-4 py-3 rounded-lg transition-all duration-200 group ${
                isActive
                  ? 'bg-gym-500/10 text-gym-400 border border-gym-500/20 shadow-[0_0_15px_rgba(6,182,212,0.15)]'
                  : 'text-slate-400 hover:bg-gym-800 hover:text-slate-100'
              }`}
            >
              <Icon className={`w-5 h-5 mr-3 ${isActive ? 'text-gym-400' : 'text-slate-500 group-hover:text-slate-300'}`} />
              <span className="font-medium">{item.label}</span>
            </button>
          );
        })}
      </nav>

      <div className="p-4 border-t border-gym-800">
        <button 
          onClick={onLogout}
          className="w-full flex items-center px-4 py-3 text-slate-400 hover:text-red-400 hover:bg-red-500/10 rounded-lg transition-colors"
        >
          <LogOut className="w-5 h-5 mr-3" />
          <span className="font-medium">Sign Out</span>
        </button>
      </div>
    </aside>
  );
};