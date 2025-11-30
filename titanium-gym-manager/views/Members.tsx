import React, { useState, useEffect } from 'react';
import { Search, Plus, Edit2, Trash2, Mail, Phone, Shield, User } from 'lucide-react';
import { Member, GymPackage, UserCreateRequest, UserUpdateRequest } from '../types';
import { Modal } from '../components/ui/Modal';
import { usersAPI } from '../api/users.api';

interface MembersProps {
  members: Member[];
  packages: GymPackage[];
  setMembers: React.Dispatch<React.SetStateAction<Member[]>>;
}

export const Members: React.FC<MembersProps> = ({ members, packages, setMembers }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingMember, setEditingMember] = useState<Member | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // Fetch users from backend
  const fetchUsers = async () => {
    try {
      const userDTOs = await usersAPI.getAllUsers();
      const mappedMembers: Member[] = userDTOs.map(dto => ({
        id: dto.id.toString(),
        fullName: dto.fullName,
        username: dto.userName,
        email: dto.email,
        phone: dto.phone,
        role: dto.role,
        status: (dto.status as any) || 'Active',
        packageId: '', // Not yet in backend
        joinDate: new Date().toISOString().split('T')[0], // Not yet in backend
        avatarUrl: `https://picsum.photos/100/100?random=${dto.id}`
      }));
      setMembers(mappedMembers);
    } catch (error) {
      console.error('Failed to fetch users:', error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // Form State
  const [formData, setFormData] = useState<Partial<Member>>({
    fullName: '',
    username: '',
    email: '',
    phone: '',
    password: '',
    role: 'TRAINER',
    status: 'Active',
    packageId: packages[0]?.id || '',
  });

  const filteredMembers = members.filter(
    (m) =>
      m.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.phone.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.role.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleOpenModal = (member?: Member) => {
    if (member) {
      setEditingMember(member);
      setFormData({ ...member, password: '' }); // Don't show existing password
    } else {
      setEditingMember(null);
      setFormData({
        fullName: '',
        username: '',
        email: '',
        phone: '',
        password: '',
        role: 'TRAINER', // Default to one of the requested roles
        status: 'Active',
        packageId: packages[0]?.id || '',
        avatarUrl: `https://picsum.photos/100/100?random=${Math.floor(Math.random() * 1000)}`
      });
    }
    setIsModalOpen(true);
  };

  const handleSave = async () => {
    if (!formData.fullName || !formData.email || !formData.username) return; // Simple validation

    setIsLoading(true);
    try {
      if (editingMember) {
        // Update
        const updateRequest: UserUpdateRequest = {
          fullName: formData.fullName,
          userName: formData.username,
          email: formData.email,
          phone: formData.phone,
          role: formData.role,
          status: formData.status
        };
        await usersAPI.updateUser(parseInt(editingMember.id), updateRequest);
      } else {
        // Create
        const createRequest: UserCreateRequest = {
          fullName: formData.fullName!,
          userName: formData.username!,
          email: formData.email!,
          phone: formData.phone!,
          password: formData.password || '123456', // Default password if not provided
          role: formData.role
        };
        await usersAPI.createUser(createRequest);
      }
      // Refresh list
      await fetchUsers();
      setIsModalOpen(false);
    } catch (error) {
      console.error('Failed to save user:', error);
      alert('Failed to save user. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Are you sure you want to remove this member?')) {
      try {
        await usersAPI.deleteUser(parseInt(id));
        await fetchUsers();
      } catch (error) {
        console.error('Failed to delete user:', error);
        alert('Failed to delete user.');
      }
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Members & Staff</h1>
          <p className="text-slate-400 mt-1">Manage user access, roles, and gym profiles.</p>
        </div>
        <button
          onClick={() => handleOpenModal()}
          className="bg-gym-500 hover:bg-gym-400 text-white px-5 py-2.5 rounded-lg flex items-center font-medium shadow-[0_0_20px_rgba(6,182,212,0.3)] transition-all transform hover:scale-105"
        >
          <Plus className="w-5 h-5 mr-2" />
          Add Member
        </button>
      </div>

      {/* Toolbar */}
      <div className="flex items-center space-x-4 bg-gym-900 border border-gym-800 p-4 rounded-xl">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-500 w-5 h-5" />
          <input
            type="text"
            placeholder="Search by name, email, username, phone or role..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full bg-gym-950 border border-gym-800 text-slate-200 pl-10 pr-4 py-2 rounded-lg focus:outline-none focus:border-gym-500 focus:ring-1 focus:ring-gym-500 transition-all placeholder:text-slate-600"
          />
        </div>
        <div className="text-sm text-slate-500">
          Showing <span className="text-white font-bold">{filteredMembers.length}</span> members
        </div>
      </div>

      {/* Table */}
      <div className="bg-gym-900 border border-gym-800 rounded-xl overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-gym-950/50 text-slate-400 text-xs uppercase tracking-wider">
                <th className="px-6 py-4 font-semibold">User Profile</th>
                <th className="px-6 py-4 font-semibold">Contact Info</th>
                <th className="px-6 py-4 font-semibold">Role</th>
                <th className="px-6 py-4 font-semibold">Package</th>
                <th className="px-6 py-4 font-semibold">Status</th>
                <th className="px-6 py-4 font-semibold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gym-800">
              {filteredMembers.map((member) => (
                <tr key={member.id} className="hover:bg-gym-800/30 transition-colors group">
                  <td className="px-6 py-4">
                    <div className="flex items-center">
                      <img src={member.avatarUrl} alt={member.fullName} className="w-10 h-10 rounded-full border border-gym-700 mr-3 object-cover" />
                      <div>
                        <div className="font-medium text-slate-200">{member.fullName}</div>
                        <div className="text-xs text-slate-500">@{member.username}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex flex-col text-sm text-slate-400">
                      <div className="flex items-center mb-1"><Mail className="w-3 h-3 mr-2" /> {member.email}</div>
                      <div className="flex items-center"><Phone className="w-3 h-3 mr-2" /> {member.phone}</div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2 py-1 rounded text-xs font-bold border flex items-center w-fit ${member.role === 'ADMIN'
                        ? 'bg-purple-500/10 text-purple-400 border-purple-500/20'
                        : member.role === 'TRAINER'
                          ? 'bg-orange-500/10 text-orange-400 border-orange-500/20'
                          : 'bg-slate-800 text-slate-400 border-slate-700'
                      }`}>
                      {member.role === 'ADMIN' && <Shield className="w-3 h-3 mr-1" />}
                      {member.role === 'TRAINER' && <User className="w-3 h-3 mr-1" />}
                      {member.role}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-3 py-1 rounded-full text-xs font-medium bg-slate-800 text-slate-300 border border-slate-700">
                      {packages.find(p => p.id === member.packageId)?.name || 'None'}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold border ${member.status === 'Active'
                        ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20'
                        : member.status === 'Inactive'
                          ? 'bg-red-500/10 text-red-400 border-red-500/20'
                          : 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20'
                      }`}>
                      {member.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <button
                        onClick={() => handleOpenModal(member)}
                        className="p-2 text-slate-400 hover:text-gym-400 hover:bg-gym-500/10 rounded-lg transition-colors"
                      >
                        <Edit2 className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => handleDelete(member.id)}
                        className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-500/10 rounded-lg transition-colors"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {filteredMembers.length === 0 && (
          <div className="p-8 text-center text-slate-500">
            No members found matching your search.
          </div>
        )}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingMember ? 'Edit Member' : 'Add New Member'}
      >
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Username</label>
              <input
                type="text"
                value={formData.username}
                onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none"
                placeholder="jdoe"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Role</label>
              <select
                value={formData.role}
                onChange={(e) => setFormData({ ...formData, role: e.target.value as any })}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none appearance-none"
              >
                <option value="ADMIN">ADMIN</option>
                <option value="TRAINER">TRAINER</option>
                {/* Keeping MEMBER option hidden in UI for new creates as per instruction "Role has 2 options", 
                    but logic supports it if editing existing MEMBER */}
                {editingMember && formData.role === 'MEMBER' && <option value="MEMBER">MEMBER</option>}
              </select>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-400 mb-1">Full Name</label>
            <input
              type="text"
              value={formData.fullName}
              onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
              className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none"
              placeholder="John Doe"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Email</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Phone</label>
              <input
                type="tel"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-400 mb-1">Password {editingMember && "(Leave blank to keep current)"}</label>
            <input
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none"
              placeholder="••••••••"
            />
          </div>

          <div className="border-t border-gym-800 pt-4 mt-2">
            <h3 className="text-sm font-semibold text-gym-400 mb-3">Gym Membership Details</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Package</label>
                <select
                  value={formData.packageId}
                  onChange={(e) => setFormData({ ...formData, packageId: e.target.value })}
                  className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none appearance-none"
                >
                  <option value="">None</option>
                  {packages.map(p => (
                    <option key={p.id} value={p.id}>{p.name}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Status</label>
                <select
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value as Member['status'] })}
                  className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 focus:border-gym-500 outline-none appearance-none"
                >
                  <option value="Active">Active</option>
                  <option value="Inactive">Inactive</option>
                  <option value="Pending">Pending</option>
                </select>
              </div>
            </div>
          </div>

          <div className="pt-4 flex justify-end space-x-3">
            <button
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2 rounded-lg text-slate-400 hover:text-slate-200 hover:bg-gym-800 transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              className="px-6 py-2 rounded-lg bg-gym-500 hover:bg-gym-400 text-white font-medium shadow-lg shadow-cyan-500/20 transition-all"
            >
              Save Member
            </button>
          </div>
          {isLoading && (
            <div className="absolute inset-0 bg-gym-950/50 flex items-center justify-center rounded-lg">
              <div className="w-8 h-8 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
            </div>
          )}
        </div>
      </Modal>
    </div>
  );
};