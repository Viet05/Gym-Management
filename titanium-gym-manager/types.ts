export type UserRole = 'ADMIN' | 'TRAINER' | 'MEMBER';

export interface Member {
  id: string;
  fullName: string;
  username: string;
  email: string;
  phone: string;
  password?: string;
  role: UserRole;
  status: 'Active' | 'Inactive' | 'Pending';
  packageId: string;
  joinDate: string;
  avatarUrl: string;
}

export interface GymPackage {
  id: string;
  name: string;
  price: number;
  durationMonths: number;
  features: string[];
  color: string;
}

export interface DashboardStats {
  totalRevenue: number;
  activeMembers: number;
  totalMembers: number;
  visitsToday: number;
  revenueTrend: number; // percentage
  memberTrend: number; // percentage
}

export type EventType = 'personal' | 'class' | 'consultation';

export interface CalendarEvent {
  id: string;
  title: string;
  start: string; // ISO String
  end: string; // ISO String
  type: EventType;
  trainerId: string;
  memberIds: string[]; // Can be multiple for classes
  notes?: string;
}

export type ViewState = 'dashboard' | 'members' | 'packages' | 'calendar';

// Backend API Types
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  userId: number;
  userName: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  fullName: string;
  email: string;
  phone: string;
  role?: UserRole;
}

// Backend UserDTO - matches backend response
export interface UserDTO {
  id: number;
  userName: string;
  fullName: string;
  email: string;
  phone: string;
  status: string;
  role: UserRole;
}

export interface UserCreateRequest {
  userName: string;
  password: string;
  fullName: string;
  email: string;
  phone: string;
  role?: UserRole;
}

export interface UserUpdateRequest {
  userName?: string;
  fullName?: string;
  email?: string;
  phone?: string;
  status?: string;
  role?: UserRole;
}

export interface PackageDTO {
  id: string;
  name: string;
  durationMonth: string;
  price: number;
  description: string;
  active: string;
  createdDate?: string;
  updatedDate?: string;
}

export interface PackageCreateRequest {
  name: string;
  durationMonth: string;
  price: number;
  description: string;
}

export interface PackageUpdateRequest {
  name?: string;
  durationMonth?: string;
  price?: number;
  description?: string;
}