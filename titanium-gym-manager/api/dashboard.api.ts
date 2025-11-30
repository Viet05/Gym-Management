import { apiClient } from './client';
import { DashboardStats } from '../types';

export const dashboardAPI = {
    getStats: async (): Promise<DashboardStats> => {
        const response = await apiClient.get<{ code: number; message: string; data: DashboardStats }>(
            '/admin/dashboard/stats'
        );
        return response.data.data;
    },
};
