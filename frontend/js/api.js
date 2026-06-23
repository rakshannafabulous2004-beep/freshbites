/**
 * FreshBite API Client
 * Handles all backend communication using Fetch API
 */

const API_BASE_URL = 'http://localhost:8080/api';

async function apiRequest(endpoint, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const config = { ...defaultOptions, ...options };
    if (config.body && typeof config.body === 'object') {
        config.body = JSON.stringify(config.body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        const data = await response.json();

        if (!response.ok) {
            const errorMessage = data.message || 'Request failed';
            throw new Error(errorMessage);
        }

        return data;
    } catch (error) {
        if (error.message === 'Failed to fetch') {
            throw new Error('Unable to connect to server. Please ensure the backend is running.');
        }
        throw error;
    }
}

// Auth API
const AuthAPI = {
    register: (userData) => apiRequest('/auth/register', { method: 'POST', body: userData }),
    login: (credentials) => apiRequest('/auth/login', { method: 'POST', body: credentials }),
};

// User API
const UserAPI = {
    getProfile: (userId) => apiRequest(`/users/${userId}`),
};

// Meals API
const MealsAPI = {
    getAll: (category) => {
        const query = category ? `?category=${encodeURIComponent(category)}` : '';
        return apiRequest(`/meals${query}`);
    },
    getById: (id) => apiRequest(`/meals/${id}`),
    create: (mealData) => apiRequest('/meals', { method: 'POST', body: mealData }),
    update: (id, mealData) => apiRequest(`/meals/${id}`, { method: 'PUT', body: mealData }),
    delete: (id) => apiRequest(`/meals/${id}`, { method: 'DELETE' }),
};

// Cart API
const CartAPI = {
    getCart: (userId) => apiRequest(`/cart/${userId}`),
    addItem: (userId, item) => apiRequest(`/cart/${userId}`, { method: 'POST', body: item }),
    updateItem: (userId, cartItemId, quantity) =>
        apiRequest(`/cart/${userId}/${cartItemId}`, { method: 'PUT', body: { quantity } }),
    removeItem: (userId, cartItemId) =>
        apiRequest(`/cart/${userId}/${cartItemId}`, { method: 'DELETE' }),
};

// Orders API
const OrdersAPI = {
    placeOrder: (userId, orderData) =>
        apiRequest(`/orders/${userId}`, { method: 'POST', body: orderData }),
    getUserOrders: (userId) => apiRequest(`/orders/user/${userId}`),
    getAllOrders: () => apiRequest('/orders'),
    updateStatus: (orderId, status) =>
        apiRequest(`/orders/${orderId}/status`, { method: 'PUT', body: { orderStatus: status } }),
};

// Session helpers
const Session = {
    getUser: () => {
        const userJson = localStorage.getItem('freshbite_user');
        return userJson ? JSON.parse(userJson) : null;
    },

    setUser: (user) => {
        localStorage.setItem('freshbite_user', JSON.stringify(user));
    },

    clearUser: () => {
        localStorage.removeItem('freshbite_user');
    },

    isLoggedIn: () => Session.getUser() !== null,

    isAdmin: () => {
        const user = Session.getUser();
        return user && user.admin === true;
    },

    getUserId: () => {
        const user = Session.getUser();
        return user ? user.id : null;
    },
};

// UI helpers
function showAlert(container, message, type = 'error') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    container.prepend(alertDiv);
    setTimeout(() => alertDiv.remove(), 5000);
}

function formatPrice(price) {
    return `₹${Number(price).toFixed(2)}`;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });
}

function getMealEmoji(category) {
    const emojis = {
        'High Protein': '🍗',
        'Low Calorie': '🥗',
        'Keto': '🐟',
        'Vegetarian': '🥙',
        'Breakfast': '🥣',
        'Low Carb': '🥬',
    };
    return emojis[category] || '🍽️';
}

function setupNavbar() {
    const toggle = document.querySelector('.nav-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (toggle && navLinks) {
        toggle.addEventListener('click', () => navLinks.classList.toggle('active'));
    }

    updateNavbarAuth();
}

function updateNavbarAuth() {
    const authLinks = document.getElementById('auth-links');
    if (!authLinks) return;

    const user = Session.getUser();

    if (user) {
        if (user.admin) {
            authLinks.innerHTML = `
                <li><a href="admin.html">Admin Panel</a></li>
                <li><a href="#" id="logout-btn">Logout</a></li>
            `;
        } else {
            authLinks.innerHTML = `
                <li><a href="profile.html">Profile</a></li>
                <li><a href="cart.html">Cart</a></li>
                <li><a href="#" id="logout-btn">Logout (${user.fullName})</a></li>
            `;
        }

        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                Session.clearUser();
                window.location.href = 'index.html';
            });
        }
    } else {
        authLinks.innerHTML = `
            <li><a href="login.html">Login</a></li>
            <li><a href="register.html" class="btn btn-primary btn-sm">Sign Up</a></li>
        `;
    }
}

function requireAuth(redirectUrl = 'login.html') {
    if (!Session.isLoggedIn()) {
        window.location.href = redirectUrl;
        return false;
    }
    if (Session.isAdmin()) {
        window.location.href = 'admin.html';
        return false;
    }
    return true;
}

function requireAdmin() {
    if (!Session.isLoggedIn() || !Session.isAdmin()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

document.addEventListener('DOMContentLoaded', setupNavbar);
