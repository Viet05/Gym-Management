import { GymPackage, Member, CalendarEvent } from './types';

export const INITIAL_PACKAGES: GymPackage[] = [
  {
    id: 'pkg_1',
    name: 'Basic Iron',
    price: 29.99,
    durationMonths: 1,
    features: ['Access to Gym Floor', 'Locker Room Access', 'Free WiFi'],
    color: 'from-slate-700 to-slate-600',
  },
  {
    id: 'pkg_2',
    name: 'Pro Athlete',
    price: 59.99,
    durationMonths: 3,
    features: ['24/7 Access', 'Group Classes', 'Sauna Access', '1 Personal Training Session'],
    color: 'from-cyan-600 to-blue-600',
  },
  {
    id: 'pkg_3',
    name: 'Elite Titan',
    price: 99.99,
    durationMonths: 12,
    features: ['All Access', 'Unlimited Classes', 'Nutrition Plan', 'Weekly PT Session', 'Guest Pass'],
    color: 'from-orange-500 to-red-600',
  },
];

export const INITIAL_MEMBERS: Member[] = [
  {
    id: 'mem_1',
    fullName: 'Alex Johnson',
    username: 'alexj',
    email: 'alex.j@example.com',
    phone: '+1 (555) 123-4567',
    role: 'MEMBER',
    status: 'Active',
    packageId: 'pkg_2',
    joinDate: '2023-11-15',
    avatarUrl: 'https://picsum.photos/100/100?random=1',
  },
  {
    id: 'mem_2',
    fullName: 'Sarah Connor',
    username: 'skynet_hater',
    email: 'sarah.c@skynet.net',
    phone: '+1 (555) 987-6543',
    role: 'TRAINER',
    status: 'Active',
    packageId: 'pkg_3',
    joinDate: '2023-08-20',
    avatarUrl: 'https://picsum.photos/100/100?random=2',
  },
  {
    id: 'mem_3',
    fullName: 'Mike Tyson',
    username: 'ironmike',
    email: 'mike@punch.com',
    phone: '+1 (555) 111-2222',
    role: 'MEMBER',
    status: 'Inactive',
    packageId: 'pkg_1',
    joinDate: '2022-05-10',
    avatarUrl: 'https://picsum.photos/100/100?random=3',
  },
  {
    id: 'mem_4',
    fullName: 'Emily Blunt',
    username: 'emilyb',
    email: 'emily@movie.com',
    phone: '+1 (555) 333-4444',
    role: 'ADMIN',
    status: 'Active',
    packageId: 'pkg_2',
    joinDate: '2024-01-05',
    avatarUrl: 'https://picsum.photos/100/100?random=4',
  },
  {
    id: 'mem_5',
    fullName: 'Chris Evans',
    username: 'cap',
    email: 'cap@marvel.com',
    phone: '+1 (555) 555-5555',
    role: 'MEMBER',
    status: 'Pending',
    packageId: 'pkg_1',
    joinDate: '2024-02-28',
    avatarUrl: 'https://picsum.photos/100/100?random=5',
  },
];

const today = new Date();
const tomorrow = new Date(today);
tomorrow.setDate(today.getDate() + 1);

export const INITIAL_EVENTS: CalendarEvent[] = [
  {
    id: 'evt_1',
    title: 'HIIT Class',
    start: new Date(today.setHours(10, 0, 0, 0)).toISOString(),
    end: new Date(today.setHours(11, 0, 0, 0)).toISOString(),
    type: 'class',
    trainerId: 'mem_2',
    memberIds: ['mem_1', 'mem_3'],
    notes: 'High intensity interval training in Room A',
  },
  {
    id: 'evt_2',
    title: 'PT Session: Alex',
    start: new Date(tomorrow.setHours(14, 0, 0, 0)).toISOString(),
    end: new Date(tomorrow.setHours(15, 0, 0, 0)).toISOString(),
    type: 'personal',
    trainerId: 'mem_2',
    memberIds: ['mem_1'],
    notes: 'Focus on leg day',
  },
];

export const MOCK_REVENUE_DATA = [
  { name: 'Mon', revenue: 1200, visits: 140 },
  { name: 'Tue', revenue: 1500, visits: 200 },
  { name: 'Wed', revenue: 1100, visits: 180 },
  { name: 'Thu', revenue: 1800, visits: 220 },
  { name: 'Fri', revenue: 2400, visits: 280 },
  { name: 'Sat', revenue: 3100, visits: 350 },
  { name: 'Sun', revenue: 2800, visits: 300 },
];

export const MOCK_DISTRIBUTION_DATA = [
  { name: 'Basic', value: 400 },
  { name: 'Pro', value: 300 },
  { name: 'Elite', value: 300 },
];