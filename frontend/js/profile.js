/**
 * Profile page - user info and order history
 */

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadProfile();
    loadOrderHistory();
});

async function loadProfile() {
    const container = document.getElementById('profile-info');
    const userId = Session.getUserId();

    try {
        const response = await UserAPI.getProfile(userId);
        const user = response.data;

        container.innerHTML = `
            <div class="card">
                <h2 style="color: var(--primary-dark); margin-bottom: 20px;">My Profile</h2>
                <div class="form-group">
                    <label>Full Name</label>
                    <p style="font-size: 1.1rem;">${user.fullName}</p>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <p style="font-size: 1.1rem;">${user.email}</p>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <p style="font-size: 1.1rem;">${user.phone}</p>
                </div>
            </div>`;
    } catch (error) {
        container.innerHTML = `<div class="alert alert-error">${error.message}</div>`;
    }
}

async function loadOrderHistory() {
    const container = document.getElementById('order-history');
    const userId = Session.getUserId();

    container.innerHTML = '<div class="loading">Loading orders...</div>';

    try {
        const response = await OrdersAPI.getUserOrders(userId);
        const orders = response.data;

        if (orders.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <h3>No orders yet</h3>
                    <p>Your order history will appear here.</p>
                    <a href="meals.html" class="btn btn-primary" style="margin-top: 12px;">Order Now</a>
                </div>`;
            return;
        }

        container.innerHTML = orders.map(order => `
            <div class="order-card">
                <div class="order-header">
                    <h3>Order #${order.id}</h3>
                    <span class="badge badge-${order.orderStatus.toLowerCase()}">${formatStatus(order.orderStatus)}</span>
                </div>
                <div class="order-meta">
                    <span>${formatDate(order.createdAt)}</span> &bull;
                    <span>${order.deliveryAddress}</span>
                </div>
                <div class="order-items-list">
                    ${order.items.map(item => `
                        <div class="order-item-row">
                            <span>${item.mealName} x${item.quantity}</span>
                            <span>${formatPrice(item.price * item.quantity)}</span>
                        </div>
                    `).join('')}
                    <div class="order-total">Total: ${formatPrice(order.totalAmount)}</div>
                </div>
            </div>
        `).join('');
    } catch (error) {
        container.innerHTML = `<div class="alert alert-error">${error.message}</div>`;
    }
}

function formatStatus(status) {
    return status.replace(/_/g, ' ');
}
