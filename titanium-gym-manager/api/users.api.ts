import { apiClient } from './client';
import { UserDTO, UserCreateRequest, UserUpdateRequest } from '../types';

export const usersAPI = {
    getAllUsers: async (filters?: Record<string, any>): Promise<UserDTO[]> => {
        const response = await apiClient.get<{ code: number; message: string; data: UserDTO[] }>(
            '/admin/dashboard/users',
            { params: filters }
        );
        return response.data.data;
    },

    createUser: async (userData: UserCreateRequest): Promise<UserDTO> => {
        const response = await apiClient.post<{ code: number; message: string; data: UserDTO }>(
            '/admin/dashboard/users',
            userData
        );
        return response.data.data;
    },

    updateUser: async (id: number, userData: UserUpdateRequest): Promise<UserDTO> => {
        const response = await apiClient.put<{ code: number; message: string; data: UserDTO }>(
            `/admin/dashboard/users/${id}`,
            userData
        );
        return response.data.data;
    },

    deleteUser: async (id: number): Promise<void> => {
        await apiClient.delete(`/admin/dashboard/users/${id}`);
    },
};
