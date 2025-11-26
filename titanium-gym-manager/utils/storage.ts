// LocalStorage utility functions for JWT token and user data

export const storage = {
    // JWT Token
    saveToken: (token: string): void => {
        localStorage.setItem('jwt_token', token);
    },

    getToken: (): string | null => {
        return localStorage.getItem('jwt_token');
    },

    removeToken: (): void => {
        localStorage.removeItem('jwt_token');
    },

    // User Data
    saveUser: (user: any): void => {
        localStorage.setItem('current_user', JSON.stringify(user));
    },

    getUser: (): any | null => {
        const userStr = localStorage.getItem('current_user');
        return userStr ? JSON.parse(userStr) : null;
    },

    removeUser: (): void => {
        localStorage.removeItem('current_user');
    },

    // Clear all auth data
    clearAuth: (): void => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('current_user');
    },
};
