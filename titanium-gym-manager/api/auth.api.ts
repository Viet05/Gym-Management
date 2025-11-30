import { apiClient } from './client';
import { AuthResponse, LoginRequest, RegisterRequest } from '../types';

export const authAPI = {
    login: async (username: string, password: string): Promise<AuthResponse> => {
        const request: LoginRequest = { username, password };
        const response = await apiClient.post<{ code: number; message: string; data: AuthResponse }>(
            '/auth/login',
            request
        );
        return response.data.data;
    },

    register: async (userData: RegisterRequest): Promise<AuthResponse> => {
        const response = await apiClient.post<{ code: number; message: string; data: AuthResponse }>(
            '/auth/register',
            userData
        );
        return response.data.data;
    },
};
