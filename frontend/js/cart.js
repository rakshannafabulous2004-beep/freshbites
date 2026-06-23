/**
 * Cart page - view, update quantity, remove items
 */

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadCart();
});

async function loadCart() {
    const container = document.getElementById('cart-container');
    const userId = Session.getUserId();

    container.innerHTML = '<div class="loading">Loading cart...</div>';

    try {
        const response = await CartAPI.getCart(userId);
        const items = response.data;
        renderCart(items);
        updateSummary(items);
    } catch (error) {
        container.innerHTML = `<div class="alert alert-error">${error.message}</div>`;
    }
}

function renderCart(items) {
    const container = document.getElementById('cart-container');

    if (items.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>Your cart is empty</h3>
                <p>Add some delicious meals to get started!</p>
                <a href="meals.html" class="btn btn-primary" style="margin-top: 16px;">Browse Meals</a>
            </div>`;
        return;
    }

    container.innerHTML = items.map(item => `
        <div class="cart-item" id="cart-item-${item.id}">
            <div class="cart-item-image">
                ${item.imageUrl
                    ? `<img src="${item.imageUrl}" alt="${item.mealName}" onerror="this.parentElement.innerHTML='🍽️'">`
                    : '🍽️'}
            </div>
            <div class="cart-item-details">
                <h3>${item.mealName}</h3>
                <p class="cart-item-price">${formatPrice(item.price)} each</p>
            </div>
            <div class="quantity-controls">
                <button class="qty-btn" onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                <span class="qty-value">${item.quantity}</span>
                <button class="qty-btn" onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
            </div>
            <div style="font-weight: 700; color: var(--primary); min-width: 80px; text-align: right;">
                ${formatPrice(item.subtotal)}
            </div>
            <button class="btn btn-danger btn-sm" onclick="removeItem(${item.id})">Remove</button>
        </div>
    `).join('');
}

function updateSummary(items) {
    const summaryContainer = document.getElementById('cart-summary');
    const checkoutBtn = document.getElementById('checkout-btn');

    if (items.length === 0) {
        summaryContainer.innerHTML = '<p class="empty-state">No items in cart</p>';
        if (checkoutBtn) checkoutBtn.style.display = 'none';
        return;
    }

    const subtotal = items.reduce((sum, item) => sum + item.subtotal, 0);

    summaryContainer.innerHTML = `
        <h3>Order Summary</h3>
        <div class="summary-row">
            <span>Items (${items.length})</span>
            <span>${formatPrice(subtotal)}</span>
        </div>
        <div class="summary-row">
            <span>Delivery</span>
            <span>Free</span>
        </div>
        <div class="summary-row total">
            <span>Total</span>
            <span>${formatPrice(subtotal)}</span>
        </div>`;

    if (checkoutBtn) checkoutBtn.style.display = 'block';
}

async function updateQuantity(cartItemId, newQuantity) {
    if (newQuantity < 1) {
        removeItem(cartItemId);
        return;
    }

    const userId = Session.getUserId();

    try {
        await CartAPI.updateItem(userId, cartItemId, newQuantity);
        loadCart();
    } catch (error) {
        alert(error.message);
    }
}

async function removeItem(cartItemId) {
    const userId = Session.getUserId();

    try {
        await CartAPI.removeItem(userId, cartItemId);
        loadCart();
    } catch (error) {
        alert(error.message);
    }
}
