/**
 * Checkout page - place order
 */

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadCheckout();
    setupCheckoutForm();
});

async function loadCheckout() {
    const userId = Session.getUserId();
    const summaryContainer = document.getElementById('checkout-summary');

    try {
        const userResponse = await UserAPI.getProfile(userId);
        const user = userResponse.data;

        document.getElementById('customerName').value = user.fullName;
        document.getElementById('phone').value = user.phone;

        const cartResponse = await CartAPI.getCart(userId);
        const items = cartResponse.data;

        if (items.length === 0) {
            summaryContainer.innerHTML = `
                <div class="empty-state">
                    <h3>Cart is empty</h3>
                    <a href="meals.html" class="btn btn-primary" style="margin-top: 12px;">Browse Meals</a>
                </div>`;
            document.getElementById('checkout-form').style.display = 'none';
            return;
        }

        const total = items.reduce((sum, item) => sum + item.subtotal, 0);

        summaryContainer.innerHTML = `
            <h3>Order Summary</h3>
            ${items.map(item => `
                <div class="summary-row">
                    <span>${item.mealName} x${item.quantity}</span>
                    <span>${formatPrice(item.subtotal)}</span>
                </div>
            `).join('')}
            <div class="summary-row total">
                <span>Total</span>
                <span>${formatPrice(total)}</span>
            </div>`;
    } catch (error) {
        summaryContainer.innerHTML = `<div class="alert alert-error">${error.message}</div>`;
    }
}

function setupCheckoutForm() {
    const form = document.getElementById('checkout-form');
    if (form) {
        form.addEventListener('submit', handlePlaceOrder);
    }
}

async function handlePlaceOrder(e) {
    e.preventDefault();
    const alertContainer = document.getElementById('alert-container');
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const userId = Session.getUserId();

    const orderData = {
        customerName: document.getElementById('customerName').value.trim(),
        phone: document.getElementById('phone').value.trim(),
        deliveryAddress: document.getElementById('deliveryAddress').value.trim(),
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Placing Order...';

    try {
        const response = await OrdersAPI.placeOrder(userId, orderData);
        showAlert(alertContainer, `Order #${response.data.id} placed successfully!`, 'success');
        setTimeout(() => window.location.href = 'profile.html', 2000);
    } catch (error) {
        showAlert(alertContainer, error.message);
        submitBtn.disabled = false;
        submitBtn.textContent = 'Place Order';
    }
}
