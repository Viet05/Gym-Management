import { apiClient } from './client';
import { PackageDTO, PackageCreateRequest, PackageUpdateRequest } from '../types';

export const packagesAPI = {
    getAllPackages: async (): Promise<PackageDTO[]> => {
        const response = await apiClient.get<{ code: number; message: string; data: PackageDTO[] }>(
            '/admin/pakages'
        );
        return response.data.data;
    },

    createPackage: async (data: PackageCreateRequest): Promise<PackageDTO> => {
        const response = await apiClient.post<{ code: number; message: string; data: PackageDTO }>(
            '/admin/pakages',
            data
        );
        return response.data.data;
    },

    updatePackage: async (id: string, data: PackageUpdateRequest): Promise<PackageDTO> => {
        const response = await apiClient.put<{ code: number; message: string; data: PackageDTO }>(
            `/admin/pakages/${id}`,
            data
        );
        return response.data.data;
    },

    deletePackage: async (id: string): Promise<void> => {
        await apiClient.delete(`/admin/pakages/${id}`);
    },
};
