import React, { useState } from 'react';
import { Dumbbell, User, Lock, Mail, Phone, ArrowLeft, CheckCircle } from 'lucide-react';
import { authAPI } from '../api/auth.api';
import { storage } from '../utils/storage';
import { RegisterRequest } from '../types';

interface RegisterProps {
  onRegister: (authResponse: any) => void;
  onNavigateToLogin: () => void;
}

export const Register: React.FC<RegisterProps> = ({ onRegister, onNavigateToLogin }) => {
  const [formData, setFormData] = useState({
    fullName: '',
    username: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords don't match");
      return;
    }

    if (formData.password.length < 6) {
      setError("Password must be at least 6 characters");
      return;
    }

    setIsLoading(true);

    try {
      const registerRequest: RegisterRequest = {
        username: formData.username,
        password: formData.password,
        fullName: formData.fullName,
        email: formData.email,
        phone: formData.phone,
        role: 'ADMIN' // Default to ADMIN for testing
      };

      const authResponse = await authAPI.register(registerRequest);
      storage.saveToken(authResponse.accessToken);
      onRegister(authResponse);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Registration failed. Please try again.';
      setError(errorMessage);
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gym-950 relative overflow-hidden py-10">
      {/* Background Elements */}
      <div className="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none">
        <div className="absolute bottom-[10%] left-[-5%] w-[600px] h-[600px] bg-purple-600/10 rounded-full blur-[100px] animate-pulse"></div>
        <div className="absolute top-[-10%] right-[-5%] w-[600px] h-[600px] bg-gym-500/10 rounded-full blur-[100px] animate-pulse" style={{ animationDelay: '2s' }}></div>
      </div>

      <div className="w-full max-w-lg p-6 relative z-10 animate-in fade-in slide-in-from-right-8 duration-500">
        <div className="bg-gym-900/80 backdrop-blur-xl border border-gym-800 rounded-2xl shadow-2xl p-8">
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-2xl font-bold text-white tracking-wider">Join Titanium</h1>
              <p className="text-slate-400 text-sm mt-1">Create your admin profile</p>
            </div>
            <div className="bg-gym-800 p-2 rounded-lg shadow-lg shadow-cyan-500/20">
              <Dumbbell className="w-8 h-8 text-gym-500" />
            </div>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {error && (
              <div className="bg-red-500/10 border border-red-500/20 rounded-lg p-3 text-red-400 text-sm">
                {error}
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-xs font-semibold text-slate-400 uppercase">Full Name</label>
                <div className="relative group">
                  <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                  <input
                    type="text"
                    value={formData.fullName}
                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                    className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                    placeholder="John Doe"
                    required
                  />
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-xs font-semibold text-slate-400 uppercase">Username</label>
                <div className="relative group">
                  <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                  <input
                    type="text"
                    value={formData.username}
                    onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                    className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                    placeholder="johnd"
                    required
                  />
                </div>
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-xs font-semibold text-slate-400 uppercase">Email Address</label>
              <div className="relative group">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                  placeholder="john@example.com"
                  required
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-xs font-semibold text-slate-400 uppercase">Phone Number</label>
              <div className="relative group">
                <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                  placeholder="+1 (555) 000-0000"
                  required
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-xs font-semibold text-slate-400 uppercase">Password</label>
                <div className="relative group">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                  <input
                    type="password"
                    value={formData.password}
                    onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                    className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                    placeholder="••••••"
                    required
                  />
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-xs font-semibold text-slate-400 uppercase">Confirm</label>
                <div className="relative group">
                  <CheckCircle className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500 group-focus-within:text-gym-400" />
                  <input
                    type="password"
                    value={formData.confirmPassword}
                    onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                    className="w-full bg-gym-950/50 border border-gym-800 rounded-lg py-2.5 pl-9 pr-4 text-slate-100 text-sm focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all"
                    placeholder="••••••"
                    required
                  />
                </div>
              </div>
            </div>

            <button
              type="submit"
              disabled={isLoading}
              className="w-full mt-6 bg-gym-500 hover:bg-gym-400 text-white font-semibold py-3 rounded-lg shadow-lg shadow-cyan-500/20 transition-all transform hover:scale-[1.02] flex items-center justify-center disabled:opacity-70 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                'Create Account'
              )}
            </button>
          </form>

          <div className="mt-6 pt-4 border-t border-gym-800 text-center">
            <button
              onClick={onNavigateToLogin}
              className="text-slate-500 hover:text-white flex items-center justify-center w-full transition-colors text-sm group"
            >
              <ArrowLeft className="w-4 h-4 mr-2 group-hover:-translate-x-1 transition-transform" />
              Back to Login
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
