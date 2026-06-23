/**
 * Authentication handlers for login and register pages
 */

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
});

async function handleLogin(e) {
    e.preventDefault();
    const alertContainer = document.getElementById('alert-container');
    const submitBtn = e.target.querySelector('button[type="submit"]');

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    submitBtn.disabled = true;
    submitBtn.textContent = 'Logging in...';

    try {
        const response = await AuthAPI.login({ email, password });
        Session.setUser(response.data);

        if (response.data.admin) {
            window.location.href = 'admin.html';
        } else {
            window.location.href = 'meals.html';
        }
    } catch (error) {
        showAlert(alertContainer, error.message);
        submitBtn.disabled = false;
        submitBtn.textContent = 'Login';
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const alertContainer = document.getElementById('alert-container');
    const submitBtn = e.target.querySelector('button[type="submit"]');

    const fullName = document.getElementById('fullName').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const phone = document.getElementById('phone').value.trim();

    submitBtn.disabled = true;
    submitBtn.textContent = 'Creating account...';

    try {
        const response = await AuthAPI.register({ fullName, email, password, phone });
        Session.setUser(response.data);
        showAlert(alertContainer, 'Account created successfully! Redirecting...', 'success');
        setTimeout(() => window.location.href = 'meals.html', 1500);
    } catch (error) {
        showAlert(alertContainer, error.message);
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create Account';
    }
}
