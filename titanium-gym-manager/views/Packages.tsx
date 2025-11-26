import React, { useState } from 'react';
import { Plus, Check, Edit, Trash2, ShieldCheck, Clock, DollarSign } from 'lucide-react';
import { GymPackage } from '../types';
import { Modal } from '../components/ui/Modal';

interface PackagesProps {
  packages: GymPackage[];
  setPackages: React.Dispatch<React.SetStateAction<GymPackage[]>>;
}

export const Packages: React.FC<PackagesProps> = ({ packages, setPackages }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPackage, setEditingPackage] = useState<GymPackage | null>(null);
  
  // Need a string for features input handling
  const [featuresInput, setFeaturesInput] = useState('');
  
  const [formData, setFormData] = useState<Partial<GymPackage>>({
    name: '',
    price: 0,
    durationMonths: 1,
    color: 'from-slate-700 to-slate-600'
  });

  const handleOpenModal = (pkg?: GymPackage) => {
    if (pkg) {
      setEditingPackage(pkg);
      setFormData(pkg);
      setFeaturesInput(pkg.features.join('\n'));
    } else {
      setEditingPackage(null);
      setFormData({
         name: '',
         price: 0,
         durationMonths: 1,
         color: 'from-slate-700 to-slate-600',
         features: []
      });
      setFeaturesInput('');
    }
    setIsModalOpen(true);
  };

  const handleSave = () => {
    if (!formData.name) return;

    const featuresArray = featuresInput.split('\n').filter(f => f.trim() !== '');
    
    if (editingPackage) {
      setPackages(prev => prev.map(p => 
        p.id === editingPackage.id ? { ...p, ...formData, features: featuresArray } as GymPackage : p
      ));
    } else {
      const newPackage: GymPackage = {
        ...(formData as GymPackage),
        id: `pkg_${Date.now()}`,
        features: featuresArray
      };
      setPackages(prev => [...prev, newPackage]);
    }
    setIsModalOpen(false);
  };

  const handleDelete = (id: string) => {
    if(window.confirm("Are you sure? This might affect members assigned to this package.")) {
        setPackages(prev => prev.filter(p => p.id !== id));
    }
  };

  const colorOptions = [
      { label: 'Slate', value: 'from-slate-700 to-slate-600' },
      { label: 'Cyan', value: 'from-cyan-600 to-blue-600' },
      { label: 'Orange', value: 'from-orange-500 to-red-600' },
      { label: 'Purple', value: 'from-purple-600 to-indigo-600' },
      { label: 'Emerald', value: 'from-emerald-600 to-green-600' },
  ];

  return (
    <div className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Membership Packages</h1>
          <p className="text-slate-400 mt-1">Configure pricing tiers and features.</p>
        </div>
        <button
          onClick={() => handleOpenModal()}
          className="bg-gym-500 hover:bg-gym-400 text-white px-5 py-2.5 rounded-lg flex items-center font-medium shadow-[0_0_20px_rgba(6,182,212,0.3)] transition-all transform hover:scale-105"
        >
          <Plus className="w-5 h-5 mr-2" />
          Create Package
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8 mt-8">
        {packages.map((pkg) => (
          <div key={pkg.id} className="relative group perspective">
            <div className="absolute -inset-1 bg-gradient-to-r from-cyan-500 to-blue-500 rounded-2xl blur opacity-25 group-hover:opacity-75 transition duration-500 group-hover:duration-200"></div>
            <div className="relative bg-gym-900 border border-gym-800 rounded-xl overflow-hidden shadow-2xl hover:-translate-y-1 transition-transform duration-300 h-full flex flex-col">
              
              {/* Header Gradient */}
              <div className={`h-2 w-full bg-gradient-to-r ${pkg.color}`}></div>
              
              <div className="p-8 flex-1 flex flex-col">
                <div className="flex justify-between items-start mb-4">
                    <h3 className="text-2xl font-bold text-white">{pkg.name}</h3>
                    <div className="flex space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button onClick={() => handleOpenModal(pkg)} className="text-slate-400 hover:text-gym-400"><Edit className="w-4 h-4"/></button>
                        <button onClick={() => handleDelete(pkg.id)} className="text-slate-400 hover:text-red-400"><Trash2 className="w-4 h-4"/></button>
                    </div>
                </div>
                
                <div className="flex items-baseline mb-6">
                  <span className="text-4xl font-extrabold text-white">${pkg.price}</span>
                  <span className="text-slate-500 ml-2">/ {pkg.durationMonths === 1 ? 'mo' : `${pkg.durationMonths} mos`}</span>
                </div>

                <div className="space-y-3 mb-8 flex-1">
                  {pkg.features.map((feature, idx) => (
                    <div key={idx} className="flex items-center text-slate-300">
                      <div className={`p-1 rounded-full bg-gym-800 mr-3 text-gym-400`}>
                        <Check className="w-3 h-3" />
                      </div>
                      <span className="text-sm">{feature}</span>
                    </div>
                  ))}
                </div>

                <button className={`w-full py-3 rounded-lg font-semibold bg-gradient-to-r ${pkg.color} text-white shadow-lg opacity-90 hover:opacity-100 transition-opacity flex justify-center items-center`}>
                  <ShieldCheck className="w-5 h-5 mr-2" />
                  View Details
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

       <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingPackage ? 'Edit Package' : 'Create Package'}
      >
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-slate-400 mb-1">Package Name</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
              placeholder="e.g. Gold Tier"
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
             <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Price ($)</label>
                <div className="relative">
                    <DollarSign className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                    <input
                    type="number"
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })}
                    className="w-full bg-gym-950 border border-gym-700 rounded-lg pl-9 pr-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
                    />
                </div>
             </div>
             <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Duration (Months)</label>
                <div className="relative">
                    <Clock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                    <input
                    type="number"
                    value={formData.durationMonths}
                    onChange={(e) => setFormData({ ...formData, durationMonths: Number(e.target.value) })}
                    className="w-full bg-gym-950 border border-gym-700 rounded-lg pl-9 pr-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
                    />
                </div>
             </div>
          </div>
          
           <div>
            <label className="block text-sm font-medium text-slate-400 mb-1">Theme Color</label>
            <div className="flex space-x-2">
                {colorOptions.map(c => (
                    <button
                        key={c.value}
                        onClick={() => setFormData({...formData, color: c.value})}
                        className={`w-8 h-8 rounded-full bg-gradient-to-r ${c.value} ${formData.color === c.value ? 'ring-2 ring-white ring-offset-2 ring-offset-gym-900' : 'opacity-50 hover:opacity-100'} transition-all`}
                    />
                ))}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-400 mb-1">Features (One per line)</label>
            <textarea
              value={featuresInput}
              onChange={(e) => setFeaturesInput(e.target.value)}
              rows={5}
              className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
              placeholder="Access to Gym&#10;Free WiFi&#10;Locker Room"
            />
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
               Save Package
             </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
