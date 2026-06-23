/**
 * Admin panel - manage meals and orders
 */

let editingMealId = null;

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAdmin()) return;
    loadAdminMeals();
    loadAdminOrders();
    setupMealForm();
});

async function loadAdminMeals() {
    const tbody = document.getElementById('meals-table-body');

    try {
        const response = await MealsAPI.getAll();
        const meals = response.data;

        tbody.innerHTML = meals.map(meal => `
            <tr>
                <td>${meal.name}</td>
                <td>${meal.category}</td>
                <td>${meal.calories}</td>
                <td>${formatPrice(meal.price)}</td>
                <td class="admin-actions">
                    <button class="btn btn-outline btn-sm" onclick="editMeal(${meal.id})">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteMeal(${meal.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="5" class="alert alert-error">${error.message}</td></tr>`;
    }
}

async function loadAdminOrders() {
    const tbody = document.getElementById('orders-table-body');

    try {
        const response = await OrdersAPI.getAllOrders();
        const orders = response.data;

        tbody.innerHTML = orders.map(order => `
            <tr>
                <td>#${order.id}</td>
                <td>${order.customerName}</td>
                <td>${order.items && order.items.length > 0 ? order.items.map(item => `${item.mealName} x${item.quantity}`).join(', ') : 'No items'}</td>
                <td>${formatPrice(order.totalAmount)}</td>
                <td>
                    <select onchange="updateOrderStatus(${order.id}, this.value)" class="status-select">
                        ${getStatusOptions(order.orderStatus)}
                    </select>
                </td>
                <td>${formatDate(order.createdAt)}</td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="5" class="alert alert-error">${error.message}</td></tr>`;
    }
}

function getStatusOptions(currentStatus) {
    const statuses = ['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED'];
    return statuses.map(status =>
        `<option value="${status}" ${status === currentStatus ? 'selected' : ''}>${status.replace(/_/g, ' ')}</option>`
    ).join('');
}

function setupMealForm() {
    const form = document.getElementById('meal-form');
    form.addEventListener('submit', handleMealSubmit);
}

async function handleMealSubmit(e) {
    e.preventDefault();
    const alertContainer = document.getElementById('admin-alert');
    const submitBtn = document.getElementById('meal-submit-btn');

    const mealData = {
        name: document.getElementById('meal-name').value.trim(),
        description: document.getElementById('meal-description').value.trim(),
        category: document.getElementById('meal-category').value,
        calories: parseInt(document.getElementById('meal-calories').value),
        protein: parseFloat(document.getElementById('meal-protein').value),
        carbs: parseFloat(document.getElementById('meal-carbs').value),
        fats: parseFloat(document.getElementById('meal-fats').value),
        price: parseFloat(document.getElementById('meal-price').value),
        imageUrl: document.getElementById('meal-image').value.trim() || null,
    };

    submitBtn.disabled = true;

    try {
        if (editingMealId) {
            await MealsAPI.update(editingMealId, mealData);
            showAlert(alertContainer, 'Meal updated successfully!', 'success');
        } else {
            await MealsAPI.create(mealData);
            showAlert(alertContainer, 'Meal created successfully!', 'success');
        }

        resetMealForm();
        loadAdminMeals();
    } catch (error) {
        showAlert(alertContainer, error.message);
    }

    submitBtn.disabled = false;
}

async function editMeal(mealId) {
    try {
        const response = await MealsAPI.getById(mealId);
        const meal = response.data;

        editingMealId = mealId;
        document.getElementById('meal-name').value = meal.name;
        document.getElementById('meal-description').value = meal.description;
        document.getElementById('meal-category').value = meal.category;
        document.getElementById('meal-calories').value = meal.calories;
        document.getElementById('meal-protein').value = meal.protein;
        document.getElementById('meal-carbs').value = meal.carbs;
        document.getElementById('meal-fats').value = meal.fats;
        document.getElementById('meal-price').value = meal.price;
        document.getElementById('meal-image').value = meal.imageUrl || '';
        document.getElementById('meal-submit-btn').textContent = 'Update Meal';
        document.getElementById('meal-cancel-btn').style.display = 'inline-block';

        document.getElementById('meal-form').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        alert(error.message);
    }
}

function resetMealForm() {
    editingMealId = null;
    document.getElementById('meal-form').reset();
    document.getElementById('meal-submit-btn').textContent = 'Add Meal';
    document.getElementById('meal-cancel-btn').style.display = 'none';
}

async function deleteMeal(mealId) {
    if (!confirm('Are you sure you want to delete this meal?')) return;

    try {
        await MealsAPI.delete(mealId);
        loadAdminMeals();
    } catch (error) {
        alert(error.message);
    }
}

async function updateOrderStatus(orderId, status) {
    try {
        await OrdersAPI.updateStatus(orderId, status);
    } catch (error) {
        alert(error.message);
        loadAdminOrders();
    }
}
