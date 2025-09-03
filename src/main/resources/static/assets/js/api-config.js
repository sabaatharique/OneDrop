// API Configuration and Common Functions
const API_CONFIG = {
    baseUrl: 'http://localhost:9090',
    endpoints: {
        donors: '/donors',
        recipients: '/recipients',
        requests: '/api/requests',
        matches: '/api/matches',
        auth: '/api/auth'
    }
};

// Common API functions
class ApiService {
    static async fetchData(endpoint, options = {}) {
        try {
            // Get authentication token if available
            const token = localStorage.getItem('authToken');
            
            const headers = {
                'Content-Type': 'application/json',
                ...options.headers
            };

            // Add authorization header if token exists
            if (token) {
                headers['Authorization'] = `Bearer ${token}`;
            }

            const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
                headers,
                ...options
            });

            if (!response.ok) {
                // Handle authentication errors
                if (response.status === 401) {
                    // Token expired or invalid, redirect to login
                    localStorage.removeItem('authToken');
                    localStorage.removeItem('userInfo');
                    window.location.href = 'login.html';
                    return;
                }
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async get(endpoint) {
        return this.fetchData(endpoint);
    }

    static async post(endpoint, data) {
        return this.fetchData(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    static async put(endpoint, data) {
        return this.fetchData(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    static async delete(endpoint) {
        return this.fetchData(endpoint, {
            method: 'DELETE'
        });
    }
}

// Dashboard statistics service
class DashboardService {
    static async getStats() {
        try {
            const [donors, recipients, requests] = await Promise.all([
                ApiService.get(API_CONFIG.endpoints.donors),
                ApiService.get(API_CONFIG.endpoints.recipients),
                ApiService.get(`${API_CONFIG.endpoints.requests}/pending`)
            ]);

            // For matches, we'll use a placeholder since the endpoint might not exist yet
            let matchesCount = 0;
            try {
                const matches = await ApiService.get(API_CONFIG.endpoints.matches);
                matchesCount = matches.length;
            } catch (error) {
                console.warn('Matches endpoint not available, using 0');
            }

            return {
                donors: donors.length,
                recipients: recipients.length,
                requests: requests.length,
                matches: matchesCount
            };
        } catch (error) {
            console.error('Error fetching dashboard stats:', error);
            return {
                donors: 0,
                recipients: 0,
                requests: 0,
                matches: 0
            };
        }
    }

    static async getLatestRequests(limit = 3) {
        try {
            const requests = await ApiService.get(`${API_CONFIG.endpoints.requests}/pending`);
            return requests.slice(0, limit);
        } catch (error) {
            console.error('Error fetching latest requests:', error);
            return [];
        }
    }

    static async getRecentMatches(limit = 3) {
        try {
            const matches = await ApiService.get(API_CONFIG.endpoints.matches);
            return matches.slice(0, limit);
        } catch (error) {
            console.warn('Matches endpoint not available, returning empty array');
            return [];
        }
    }
}

// Utility functions
const Utils = {
    formatDate: (dateString) => {
        if (!dateString) return '-';
        return new Date(dateString).toLocaleDateString();
    },

    formatStatus: (status) => {
        const statusMap = {
            'PENDING': 'Pending',
            'APPROVED': 'Approved',
            'MATCHED': 'Matched',
            'COMPLETED': 'Completed',
            'CANCELLED': 'Cancelled',
            'REJECTED': 'Rejected'
        };
        return statusMap[status] || status;
    },

    showError: (element, message) => {
        if (element) {
            element.innerHTML = `<tr><td colspan="100%" style="text-align:center; color:red; padding:10px;">${message}</td></tr>`;
        }
    },

    showLoading: (element) => {
        if (element) {
            element.innerHTML = `<tr><td colspan="100%" style="text-align:center; padding:10px;">Loading...</td></tr>`;
        }
    }
};

// Export for use in other files
window.API_CONFIG = API_CONFIG;
window.ApiService = ApiService;
window.DashboardService = DashboardService;
window.Utils = Utils;
