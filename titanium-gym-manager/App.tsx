import React, { useState, useEffect } from 'react';
import { Sidebar } from './components/Sidebar';
import { Dashboard } from './views/Dashboard';
import { Members } from './views/Members';
import { Packages } from './views/Packages';
import { Calendar } from './views/Calendar';
import { Login } from './views/Login';
import { Register } from './views/Register';
import { Member, GymPackage, ViewState, CalendarEvent, AuthResponse } from './types';
import { INITIAL_MEMBERS, INITIAL_PACKAGES, INITIAL_EVENTS } from './constants';
import { storage } from './utils/storage';
import { usersAPI } from './api/users.api';

// Auth State types
type AuthView = 'login' | 'register' | 'app';

const App: React.FC = () => {
  const [authView, setAuthView] = useState<AuthView>('login');
  const [currentUser, setCurrentUser] = useState<Member | null>(null);
  const [currentView, setCurrentView] = useState<ViewState>('dashboard');

  // State for data - using mock data for packages and events (backend endpoints not implemented yet)
  const [members, setMembers] = useState<Member[]>([]);
  const [packages, setPackages] = useState<GymPackage[]>(INITIAL_PACKAGES);
  const [events, setEvents] = useState<CalendarEvent[]>(INITIAL_EVENTS);

  // Check for existing session on app load
  useEffect(() => {
    const token = storage.getToken();
    const savedUser = storage.getUser();
    if (token && savedUser) {
      setCurrentUser(savedUser);
      setAuthView('app');
    }
  }, []);

  const handleLogin = (authResponse: AuthResponse) => {
    // Create a simple user object from AuthResponse
    // In a full implementation, you'd fetch full user details from /api/users/{userId}
    const user: Member = {
      id: authResponse.userId.toString(),
      username: authResponse.userName,
      fullName: authResponse.userName, // Backend doesn't return fullName in auth response
      email: '', // Would fetch from user details
      phone: '',
      role: 'ADMIN', // Assume admin for now, would come from user details
      status: 'Active',
      packageId: '',
      joinDate: new Date().toISOString().split('T')[0],
      avatarUrl: `https://picsum.photos/100/100?random=${authResponse.userId}`
    };

    storage.saveUser(user);
    setCurrentUser(user);
    setAuthView('app');
  };

  const handleRegister = (authResponse: AuthResponse) => {
    // Similar to login, create user object from auth response
    const user: Member = {
      id: authResponse.userId.toString(),
      username: authResponse.userName,
      fullName: authResponse.userName,
      email: '',
      phone: '',
      role: 'ADMIN',
      status: 'Active',
      packageId: '',
      joinDate: new Date().toISOString().split('T')[0],
      avatarUrl: `https://picsum.photos/100/100?random=${authResponse.userId}`
    };

    storage.saveUser(user);
    setCurrentUser(user);
    setAuthView('app');
  };

  const handleLogout = () => {
    storage.clearAuth();
    setCurrentUser(null);
    setAuthView('login');
    setCurrentView('dashboard');
  };

  const renderAppView = () => {
    switch (currentView) {
      case 'dashboard':
        return <Dashboard />;
      case 'members':
        return <Members members={members} packages={packages} setMembers={setMembers} />;
      case 'packages':
        return <Packages packages={packages} setPackages={setPackages} />;
      case 'calendar':
        return <Calendar events={events} members={members} setEvents={setEvents} />;
      default:
        return <Dashboard />;
    }
  };

  // Auth Flow Handling
  if (authView === 'login') {
    return <Login onLogin={handleLogin} onNavigateToRegister={() => setAuthView('register')} />;
  }

  if (authView === 'register') {
    return <Register onRegister={handleRegister} onNavigateToLogin={() => setAuthView('login')} />;
  }

  // Main App Flow
  return (
    <div className="flex min-h-screen bg-gym-950 text-slate-100">
      <Sidebar
        currentView={currentView}
        onNavigate={setCurrentView}
        onLogout={handleLogout}
      />

      <main className="flex-1 ml-64 p-8 relative overflow-hidden">
        {/* Decorative Background Elements */}
        <div className="absolute top-0 left-0 w-full h-full overflow-hidden -z-10 pointer-events-none">
          <div className="absolute top-[-10%] right-[-5%] w-96 h-96 bg-cyan-500/5 rounded-full blur-3xl"></div>
          <div className="absolute bottom-[10%] left-[10%] w-64 h-64 bg-blue-600/5 rounded-full blur-3xl"></div>
        </div>

        {/* User Greeting / Top Bar Area (Optional, could be in Dashboard) */}
        {currentUser && (
          <div className="absolute top-8 right-8 flex items-center space-x-3 z-10">
            <div className="text-right hidden sm:block">
              <div className="text-sm font-bold text-white">{currentUser.fullName}</div>
              <div className="text-xs text-gym-400">{currentUser.role}</div>
            </div>
            <img
              src={currentUser.avatarUrl}
              alt="Profile"
              className="w-10 h-10 rounded-full border-2 border-gym-800 shadow-sm"
            />
          </div>
        )}

        {renderAppView()}
      </main>
    </div>
  );
};

export default App;