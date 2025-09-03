// Authentication JavaScript functions
const AUTH_CONFIG = {
    baseUrl: 'http://localhost:9090',
    endpoints: {
        login: '/api/auth/login',
        register: '/api/auth/register'
    }
};

// Authentication service class
class AuthService {
    static async login(email, password, role) {
        try {
            const response = await fetch(`${AUTH_CONFIG.baseUrl}${AUTH_CONFIG.endpoints.login}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                    role: role
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: 'Login failed' }));
                throw new Error(errorData.message || 'Login failed');
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    }

    static async register(userData) {
        try {
            const response = await fetch(`${AUTH_CONFIG.baseUrl}${AUTH_CONFIG.endpoints.register}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: 'Registration failed' }));
                throw new Error(errorData.message || 'Registration failed');
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    }

    static saveToken(token) {
        localStorage.setItem('authToken', token);
    }

    static getToken() {
        return localStorage.getItem('authToken');
    }

    static removeToken() {
        localStorage.removeItem('authToken');
    }

    static saveUserInfo(userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
    }

    static getUserInfo() {
        const userInfo = localStorage.getItem('userInfo');
        return userInfo ? JSON.parse(userInfo) : null;
    }

    static removeUserInfo() {
        localStorage.removeItem('userInfo');
    }

    static isAuthenticated() {
        return !!this.getToken();
    }

    static logout() {
        this.removeToken();
        this.removeUserInfo();
        window.location.href = 'login.html';
    }
}

// Utility functions
const AuthUtils = {
    showMessage: (elementId, message, isError = false) => {
        const messageElement = document.getElementById(elementId);
        if (messageElement) {
            messageElement.textContent = message;
            messageElement.style.color = isError ? 'red' : 'green';
            messageElement.style.display = 'block';
        }
    },

    hideMessage: (elementId) => {
        const messageElement = document.getElementById(elementId);
        if (messageElement) {
            messageElement.style.display = 'none';
        }
    },

    redirectToDashboard: (role) => {
        // Redirect based on user role
        switch (role) {
            case 'DONOR':
                window.location.href = 'donor_dash.html';
                break;
            case 'RECIPIENT':
                window.location.href = 'recipient_dash.html';
                break;
            default:
                window.location.href = 'dashboard.html';
        }
    },

    validateEmail: (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    validatePassword: (password) => {
        return password.length >= 8;
    },

    validatePhone: (phone) => {
        const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
        return phoneRegex.test(phone.replace(/\s/g, ''));
    }
};

// Export for use in other files
window.AuthService = AuthService;
window.AuthUtils = AuthUtils;
window.AUTH_CONFIG = AUTH_CONFIG;
