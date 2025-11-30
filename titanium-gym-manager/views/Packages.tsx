import React, { useState, useEffect } from 'react';
import { Plus, Check, Edit, Trash2, ShieldCheck, Clock, DollarSign, Loader } from 'lucide-react';
import { GymPackage, PackageDTO } from '../types';
import { Modal } from '../components/ui/Modal';
import { packagesAPI } from '../api/packages.api';

interface PackagesProps {
  packages: GymPackage[];
  setPackages: React.Dispatch<React.SetStateAction<GymPackage[]>>;
}

export const Packages: React.FC<PackagesProps> = ({ packages, setPackages }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPackage, setEditingPackage] = useState<GymPackage | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // Need a string for features input handling
  const [featuresInput, setFeaturesInput] = useState('');

  const [formData, setFormData] = useState<Partial<GymPackage>>({
    name: '',
    price: 0,
    durationMonths: 1,
    color: 'from-slate-700 to-slate-600'
  });

  const colorOptions = [
    { label: 'Slate', value: 'from-slate-700 to-slate-600' },
    { label: 'Cyan', value: 'from-cyan-600 to-blue-600' },
    { label: 'Orange', value: 'from-orange-500 to-red-600' },
    { label: 'Purple', value: 'from-purple-600 to-indigo-600' },
    { label: 'Emerald', value: 'from-emerald-600 to-green-600' },
  ];

  const getRandomColor = () => {
    const randomIndex = Math.floor(Math.random() * colorOptions.length);
    return colorOptions[randomIndex].value;
  };

  const fetchPackages = async () => {
    setIsLoading(true);
    try {
      const data = await packagesAPI.getAllPackages();
      const mappedPackages: GymPackage[] = data.map(dto => ({
        id: dto.id,
        name: dto.name,
        price: dto.price,
        durationMonths: parseInt(dto.durationMonth) || 1,
        features: dto.description ? dto.description.split('\n') : [],
        color: getRandomColor() // Backend doesn't store color, so we assign one
      }));
      setPackages(mappedPackages);
    } catch (error) {
      console.error("Failed to fetch packages", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPackages();
  }, []);

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

  const handleSave = async () => {
    if (!formData.name) return;

    const featuresArray = featuresInput.split('\n').filter(f => f.trim() !== '');
    const description = featuresArray.join('\n');

    setIsLoading(true);
    try {
      if (editingPackage) {
        await packagesAPI.updatePackage(editingPackage.id, {
          name: formData.name,
          price: formData.price,
          durationMonth: formData.durationMonths?.toString(),
          description: description
        });
      } else {
        await packagesAPI.createPackage({
          name: formData.name!,
          price: formData.price!,
          durationMonth: formData.durationMonths?.toString() || "1",
          description: description
        });
      }
      await fetchPackages();
      setIsModalOpen(false);
    } catch (error) {
      console.error("Failed to save package", error);
      alert("Failed to save package");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm("Are you sure? This might affect members assigned to this package.")) {
      setIsLoading(true);
      try {
        await packagesAPI.deletePackage(id);
        await fetchPackages();
      } catch (error) {
        console.error("Failed to delete package", error);
        alert("Failed to delete package");
      } finally {
        setIsLoading(false);
      }
    }
  };

  if (isLoading && packages.length === 0) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader className="w-8 h-8 text-gym-500 animate-spin" />
      </div>
    );
  }

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
                    <button onClick={() => handleOpenModal(pkg)} className="text-slate-400 hover:text-gym-400"><Edit className="w-4 h-4" /></button>
                    <button onClick={() => handleDelete(pkg.id)} className="text-slate-400 hover:text-red-400"><Trash2 className="w-4 h-4" /></button>
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

          {/* Color selection removed from UI as backend doesn't support it, or keep it visual only? 
               User asked to match backend. Backend has no color. 
               But UI looks good with color. I'll keep it visual-only for now, 
               but maybe hide the input since it won't persist? 
               Actually, I'll hide the color picker since it's misleading if it doesn't save.
               Wait, I can keep it in the form data but it won't be saved. 
               Let's remove the color picker to be honest with the user, 
               or keep it and warn. 
               I'll remove the color picker from the modal to simplify and match backend capabilities.
           */}

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

          <div className="pt-4 flex-end flex justify-end space-x-3">
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
              {isLoading ? 'Saving...' : 'Save Package'}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
