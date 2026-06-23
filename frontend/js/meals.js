/**
 * Meals page - browse, filter, view details, add to cart
 */

let allMeals = [];
let activeCategory = '';

document.addEventListener('DOMContentLoaded', async () => {
    await loadMeals();
    setupFilters();
    setupSearch();
});

async function loadMeals(category = '') {
    const container = document.getElementById('meals-container');
    container.innerHTML = '<div class="loading">Loading meals...</div>';

    try {
        const response = await MealsAPI.getAll(category || undefined);
        allMeals = response.data;
        renderMeals(allMeals);
    } catch (error) {
        container.innerHTML = `<div class="alert alert-error">${error.message}</div>`;
    }
}

function renderMeals(meals) {
    const container = document.getElementById('meals-container');

    if (meals.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>No meals found</h3>
                <p>Try a different category or search term.</p>
            </div>`;
        return;
    }

    container.innerHTML = meals.map(meal => `
        <div class="meal-card">
            <div class="meal-image">
                ${meal.imageUrl
                    ? `<img src="${meal.imageUrl}" alt="${meal.name}" onerror="this.parentElement.innerHTML='${getMealEmoji(meal.category)}'">`
                    : getMealEmoji(meal.category)}
            </div>
            <div class="meal-body">
                <span class="meal-category">${meal.category}</span>
                <h3>${meal.name}</h3>
                <p class="meal-description">${meal.description}</p>
                <div class="nutrition-info">
                    <span class="nutrition-item"><strong>${meal.calories}</strong> cal</span>
                    <span class="nutrition-item">P: <strong>${meal.protein}g</strong></span>
                    <span class="nutrition-item">C: <strong>${meal.carbs}g</strong></span>
                    <span class="nutrition-item">F: <strong>${meal.fats}g</strong></span>
                </div>
                <div class="meal-footer">
                    <span class="meal-price">${formatPrice(meal.price)}</span>
                    <div>
                        <button class="btn btn-outline btn-sm" onclick="showMealDetail(${meal.id})">Details</button>
                        <button class="btn btn-primary btn-sm" onclick="addToCart(${meal.id})">Add to Cart</button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

function setupFilters() {
    const filtersContainer = document.getElementById('category-filters');
    const categories = ['All', 'High Protein', 'Low Calorie', 'Keto', 'Vegetarian', 'Breakfast', 'Low Carb'];

    filtersContainer.innerHTML = categories.map(cat => `
        <button class="filter-btn ${cat === 'All' ? 'active' : ''}"
                onclick="filterByCategory('${cat === 'All' ? '' : cat}')">${cat}</button>
    `).join('');
}

function filterByCategory(category) {
    activeCategory = category;
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
        const isAll = category === '' && btn.textContent.trim() === 'All';
        const isMatch = btn.textContent.trim() === category;
        if (isAll || isMatch) {
            btn.classList.add('active');
        }
    });

    if (category) {
        loadMeals(category);
    } else {
        loadMeals();
    }
}

function setupSearch() {
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const term = e.target.value.toLowerCase();
            const filtered = allMeals.filter(meal =>
                meal.name.toLowerCase().includes(term) ||
                meal.description.toLowerCase().includes(term) ||
                meal.category.toLowerCase().includes(term)
            );
            renderMeals(filtered);
        });
    }
}

async function showMealDetail(mealId) {
    try {
        const response = await MealsAPI.getById(mealId);
        const meal = response.data;

        const modal = document.getElementById('meal-modal');
        modal.innerHTML = `
            <div class="modal-overlay" onclick="closeModal(event)">
                <div class="modal" onclick="event.stopPropagation()">
                    <button class="modal-close" onclick="closeModal()">&times;</button>
                    <span class="meal-category">${meal.category}</span>
                    <h2>${meal.name}</h2>
                    <p>${meal.description}</p>
                    <div class="modal-nutrition">
                        <div class="nutrition-box">
                            <div class="value">${meal.calories}</div>
                            <div class="label">Calories</div>
                        </div>
                        <div class="nutrition-box">
                            <div class="value">${meal.protein}g</div>
                            <div class="label">Protein</div>
                        </div>
                        <div class="nutrition-box">
                            <div class="value">${meal.carbs}g</div>
                            <div class="label">Carbs</div>
                        </div>
                        <div class="nutrition-box">
                            <div class="value">${meal.fats}g</div>
                            <div class="label">Fats</div>
                        </div>
                    </div>
                    <div class="meal-footer">
                        <span class="meal-price">${formatPrice(meal.price)}</span>
                        <div class="modal-actions">
                            <button class="btn btn-outline" onclick="addToCart(${meal.id}); closeModal();">Add to Cart</button>
                            <button class="btn btn-primary" onclick="orderNow(${meal.id}); closeModal();">Order Now</button>
                        </div>
                    </div>
                </div>
            </div>`;
        modal.classList.remove('hidden');
    } catch (error) {
        alert(error.message);
    }
}

function closeModal(event) {
    if (event && event.target !== event.currentTarget) return;
    const modal = document.getElementById('meal-modal');
    modal.classList.add('hidden');
    modal.innerHTML = '';
}

async function addToCart(mealId) {
    if (!Session.isLoggedIn()) {
        window.location.href = 'login.html';
        return;
    }

    if (Session.isAdmin()) {
        alert('Admin accounts cannot add items to cart.');
        return;
    }

    const userId = Session.getUserId();

    try {
        await CartAPI.addItem(userId, { mealId, quantity: 1 });
        alert('Added to cart!');
    } catch (error) {
        alert(error.message);
    }
}

async function orderNow(mealId) {
    if (!Session.isLoggedIn()) {
        window.location.href = 'login.html';
        return;
    }

    if (Session.isAdmin()) {
        alert('Admin accounts cannot place orders.');
        return;
    }

    const userId = Session.getUserId();

    try {
        await CartAPI.addItem(userId, { mealId, quantity: 1 });
        window.location.href = 'checkout.html';
    } catch (error) {
        alert(error.message);
    }
}
