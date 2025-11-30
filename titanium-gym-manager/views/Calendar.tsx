import React, { useState } from 'react';
import { CalendarEvent, Member, EventType } from '../types';
import { ChevronLeft, ChevronRight, Plus, Users, User, Clock, Filter, Trash2 } from 'lucide-react';
import { Modal } from '../components/ui/Modal';

interface CalendarProps {
  events: CalendarEvent[];
  members: Member[];
  setEvents: React.Dispatch<React.SetStateAction<CalendarEvent[]>>;
}

export const Calendar: React.FC<CalendarProps> = ({ events, members, setEvents }) => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [filterTrainer, setFilterTrainer] = useState<string>('all');
  
  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDateSlot, setSelectedDateSlot] = useState<Date | null>(null);
  const [editingEvent, setEditingEvent] = useState<CalendarEvent | null>(null);

  // Form State
  const [formData, setFormData] = useState<Partial<CalendarEvent>>({
    title: '',
    type: 'personal',
    trainerId: '',
    memberIds: [],
    start: '',
    end: '',
    notes: ''
  });

  const trainers = members.filter(m => m.role === 'TRAINER');
  const regularMembers = members.filter(m => m.role === 'MEMBER');

  // Calendar Logic
  const getDaysInMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
  };

  const getFirstDayOfMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth(), 1).getDay();
  };

  const handlePrevMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const handleNextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const handleToday = () => {
    setCurrentDate(new Date());
  };

  // Filter Events
  const filteredEvents = events.filter(event => {
    if (filterTrainer === 'all') return true;
    return event.trainerId === filterTrainer;
  });

  // Event Handlers
  const handleOpenModal = (date?: Date, event?: CalendarEvent) => {
    if (event) {
      setEditingEvent(event);
      setFormData({ ...event });
      setSelectedDateSlot(null);
    } else if (date) {
      setEditingEvent(null);
      // Set default time to 9:00 AM on selected date
      const startDate = new Date(date);
      startDate.setHours(9, 0, 0, 0);
      const endDate = new Date(date);
      endDate.setHours(10, 0, 0, 0); // 1 hour duration default

      // ISO string for datetime-local input needs YYYY-MM-DDThh:mm format
      const formatForInput = (d: Date) => d.toISOString().slice(0, 16);

      setFormData({
        title: '',
        type: 'personal',
        trainerId: trainers[0]?.id || '',
        memberIds: [],
        start: formatForInput(startDate),
        end: formatForInput(endDate),
        notes: ''
      });
      setSelectedDateSlot(date);
    }
    setIsModalOpen(true);
  };

  const handleSave = () => {
    if (!formData.title || !formData.start || !formData.end || !formData.trainerId) return;

    if (editingEvent) {
      setEvents(prev => prev.map(e => 
        e.id === editingEvent.id ? { ...e, ...formData } as CalendarEvent : e
      ));
    } else {
      const newEvent: CalendarEvent = {
        ...(formData as CalendarEvent),
        id: `evt_${Date.now()}`,
      };
      setEvents(prev => [...prev, newEvent]);
    }
    setIsModalOpen(false);
  };

  const handleDelete = () => {
    if (editingEvent && window.confirm('Are you sure you want to delete this event?')) {
      setEvents(prev => prev.filter(e => e.id !== editingEvent.id));
      setIsModalOpen(false);
    }
  };

  // Helper to check if a day has events
  const getEventsForDay = (day: number) => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const checkStart = new Date(year, month, day, 0, 0, 0);
    const checkEnd = new Date(year, month, day, 23, 59, 59);

    return filteredEvents.filter(event => {
      const eventStart = new Date(event.start);
      return eventStart >= checkStart && eventStart <= checkEnd;
    }).sort((a, b) => new Date(a.start).getTime() - new Date(b.start).getTime());
  };

  // Render Grid
  const renderCalendarGrid = () => {
    const daysInMonth = getDaysInMonth(currentDate);
    const firstDay = getFirstDayOfMonth(currentDate);
    const days = [];
    const weeks = [];
    
    // Empty cells for previous month
    for (let i = 0; i < firstDay; i++) {
      days.push(<div key={`empty-${i}`} className="h-32 bg-gym-950/30 border border-gym-800/50"></div>);
    }

    // Days of month
    for (let day = 1; day <= daysInMonth; day++) {
      const dayEvents = getEventsForDay(day);
      const isToday = 
        day === new Date().getDate() && 
        currentDate.getMonth() === new Date().getMonth() && 
        currentDate.getFullYear() === new Date().getFullYear();

      days.push(
        <div 
          key={`day-${day}`} 
          onClick={() => handleOpenModal(new Date(currentDate.getFullYear(), currentDate.getMonth(), day))}
          className={`h-32 border border-gym-800 p-2 relative group hover:bg-gym-800/30 transition-colors cursor-pointer overflow-hidden ${isToday ? 'bg-gym-500/5' : 'bg-gym-900'}`}
        >
          <div className="flex justify-between items-start mb-1">
             <span className={`text-sm font-medium w-7 h-7 flex items-center justify-center rounded-full ${isToday ? 'bg-gym-500 text-white' : 'text-slate-400 group-hover:text-slate-200'}`}>
               {day}
             </span>
             {isToday && <span className="text-[10px] uppercase font-bold text-gym-400">Today</span>}
          </div>
          
          <div className="space-y-1 overflow-y-auto max-h-[calc(100%-2rem)] custom-scrollbar">
            {dayEvents.map(event => (
              <div 
                key={event.id}
                onClick={(e) => { e.stopPropagation(); handleOpenModal(undefined, event); }}
                className={`text-xs p-1.5 rounded border mb-1 truncate shadow-sm hover:scale-[1.02] transition-transform ${
                  event.type === 'class' 
                    ? 'bg-purple-500/10 border-purple-500/30 text-purple-300' 
                    : 'bg-cyan-500/10 border-cyan-500/30 text-cyan-300'
                }`}
              >
                <div className="flex items-center">
                   <div className={`w-1.5 h-1.5 rounded-full mr-1.5 ${event.type === 'class' ? 'bg-purple-500' : 'bg-cyan-400'}`}></div>
                   <span className="font-semibold mr-1">{new Date(event.start).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</span>
                   {event.title}
                </div>
              </div>
            ))}
          </div>
          
          {/* Add overlay */}
          <div className="absolute inset-0 bg-white/5 opacity-0 group-hover:opacity-100 pointer-events-none transition-opacity flex items-center justify-center">
             <Plus className="text-white/20 w-8 h-8" />
          </div>
        </div>
      );
    }

    return days;
  };

  const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  return (
    <div className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500 h-[calc(100vh-8rem)] flex flex-col">
      {/* Header */}
      <div className="flex flex-col md:flex-row justify-between items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Schedule</h1>
          <p className="text-slate-400 mt-1">Manage trainer sessions and classes.</p>
        </div>
        
        <div className="flex items-center space-x-4 bg-gym-900 p-2 rounded-lg border border-gym-800">
          <div className="flex items-center px-3 border-r border-gym-800">
             <Filter className="w-4 h-4 text-slate-500 mr-2" />
             <select 
               className="bg-transparent text-sm text-slate-300 outline-none cursor-pointer"
               value={filterTrainer}
               onChange={(e) => setFilterTrainer(e.target.value)}
             >
               <option value="all">All Trainers</option>
               {trainers.map(t => (
                 <option key={t.id} value={t.id}>{t.fullName}</option>
               ))}
             </select>
          </div>
          <div className="flex items-center space-x-2">
            <button onClick={handlePrevMonth} className="p-1 hover:bg-gym-800 rounded-full text-slate-400 hover:text-white transition-colors">
              <ChevronLeft className="w-5 h-5" />
            </button>
            <span className="font-bold text-lg w-32 text-center text-slate-200">
              {currentDate.toLocaleString('default', { month: 'long', year: 'numeric' })}
            </span>
            <button onClick={handleNextMonth} className="p-1 hover:bg-gym-800 rounded-full text-slate-400 hover:text-white transition-colors">
              <ChevronRight className="w-5 h-5" />
            </button>
          </div>
          <button 
            onClick={handleToday}
            className="px-3 py-1 bg-gym-800 hover:bg-gym-700 text-xs font-medium text-slate-300 rounded border border-gym-700 transition-colors"
          >
            Today
          </button>
        </div>
      </div>

      {/* Calendar Grid */}
      <div className="flex-1 bg-gym-900 border border-gym-800 rounded-xl overflow-hidden shadow-2xl flex flex-col">
        {/* Weekday Header */}
        <div className="grid grid-cols-7 bg-gym-950 border-b border-gym-800">
          {weekDays.map(day => (
            <div key={day} className="py-3 text-center text-sm font-semibold text-slate-500 uppercase tracking-wider">
              {day}
            </div>
          ))}
        </div>
        
        {/* Days Grid */}
        <div className="grid grid-cols-7 flex-1 overflow-y-auto">
          {renderCalendarGrid()}
        </div>
      </div>

      {/* Add/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingEvent ? 'Edit Schedule' : 'New Session'}
      >
        <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Event Title</label>
              <input
                type="text"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
                placeholder="e.g. Morning HIIT"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-slate-400 mb-1">Type</label>
                  <div className="flex space-x-2">
                     <button 
                       onClick={() => setFormData({...formData, type: 'personal'})}
                       className={`flex-1 py-2 rounded-lg text-sm border flex items-center justify-center ${formData.type === 'personal' ? 'bg-cyan-500/20 border-cyan-500 text-cyan-400' : 'bg-gym-950 border-gym-700 text-slate-400'}`}
                     >
                       <User className="w-4 h-4 mr-2" /> PT
                     </button>
                     <button 
                       onClick={() => setFormData({...formData, type: 'class'})}
                       className={`flex-1 py-2 rounded-lg text-sm border flex items-center justify-center ${formData.type === 'class' ? 'bg-purple-500/20 border-purple-500 text-purple-400' : 'bg-gym-950 border-gym-700 text-slate-400'}`}
                     >
                       <Users className="w-4 h-4 mr-2" /> Class
                     </button>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-400 mb-1">Trainer</label>
                  <select
                    value={formData.trainerId}
                    onChange={(e) => setFormData({ ...formData, trainerId: e.target.value })}
                    className="w-full bg-gym-950 border border-gym-700 rounded-lg px-3 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
                  >
                    <option value="" disabled>Select Trainer</option>
                    {trainers.map(t => (
                      <option key={t.id} value={t.id}>{t.fullName}</option>
                    ))}
                  </select>
                </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
                <div>
                   <label className="block text-sm font-medium text-slate-400 mb-1">Start Time</label>
                   <input
                     type="datetime-local"
                     value={formData.start}
                     onChange={(e) => setFormData({ ...formData, start: e.target.value })}
                     className="w-full bg-gym-950 border border-gym-700 rounded-lg px-3 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none text-sm"
                   />
                </div>
                <div>
                   <label className="block text-sm font-medium text-slate-400 mb-1">End Time</label>
                   <input
                     type="datetime-local"
                     value={formData.end}
                     onChange={(e) => setFormData({ ...formData, end: e.target.value })}
                     className="w-full bg-gym-950 border border-gym-700 rounded-lg px-3 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none text-sm"
                   />
                </div>
            </div>

            {/* Member Selection - Simple Multi-select representation */}
            <div>
               <label className="block text-sm font-medium text-slate-400 mb-1">
                 {formData.type === 'personal' ? 'Client' : 'Attendees'}
               </label>
               <select
                 multiple={formData.type === 'class'}
                 value={formData.type === 'personal' ? formData.memberIds?.[0] || '' : formData.memberIds}
                 onChange={(e) => {
                    if (formData.type === 'personal') {
                        setFormData({...formData, memberIds: [e.target.value]});
                    } else {
                        // Multi-select logic for classes
                        const selectedOptions = Array.from(e.target.selectedOptions, option => option.value);
                        setFormData({...formData, memberIds: selectedOptions});
                    }
                 }}
                 className="w-full bg-gym-950 border border-gym-700 rounded-lg px-3 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none h-24 overflow-y-auto"
               >
                 {formData.type === 'personal' && <option value="" disabled>Select Client</option>}
                 {regularMembers.map(m => (
                    <option key={m.id} value={m.id}>{m.fullName} (@{m.username})</option>
                 ))}
               </select>
               <p className="text-xs text-slate-500 mt-1">
                 {formData.type === 'class' ? 'Hold Ctrl/Cmd to select multiple members.' : ''}
               </p>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-slate-400 mb-1">Notes</label>
              <textarea
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                rows={3}
                className="w-full bg-gym-950 border border-gym-700 rounded-lg px-4 py-2 text-slate-100 focus:ring-1 focus:ring-gym-500 outline-none"
                placeholder="Room number, equipment needed, etc."
              />
            </div>

            <div className="pt-4 flex justify-between">
                {editingEvent ? (
                     <button
                     onClick={handleDelete}
                     className="px-4 py-2 rounded-lg text-red-400 hover:bg-red-500/10 border border-transparent hover:border-red-500/20 transition-colors flex items-center"
                   >
                     <Trash2 className="w-4 h-4 mr-2" /> Delete
                   </button>
                ) : <div></div>}
               
               <div className="flex space-x-3">
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
                   {editingEvent ? 'Update' : 'Schedule'}
                 </button>
               </div>
            </div>
        </div>
      </Modal>
    </div>
  );
};