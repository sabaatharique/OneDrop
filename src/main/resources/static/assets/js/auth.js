document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const messageDiv = document.getElementById('message');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            messageDiv.textContent = '';

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const role = document.getElementById('role').value;

            try {
                const response = await axios.post('/api/auth/login', {
                    email,
                    password,
                    role
                });

                if (response.data && response.data.token) {
                    localStorage.setItem('jwtToken', response.data.token);
                    localStorage.setItem('userRole', role); // Store the role for dynamic navigation
                    localStorage.setItem('userId', response.data.userId); // Store the userId

                    if (response.data.recipientId) {
                        localStorage.setItem('recipientId', response.data.recipientId);
                        localStorage.removeItem('donorId');
                    }
                    if (response.data.donorId) {
                        localStorage.setItem('donorId', response.data.donorId);
                        localStorage.removeItem('recipientId');
                    }

                    // Redirect based on role
                    const userRole = response.data.role || role;

                    if (userRole === 'DONOR') {
                        localStorage.setItem('userRole', 'DONOR');
                        window.location.href = 'profile_page.html';
                    } else if (userRole === 'RECIPIENT') {
                        localStorage.setItem('userRole', 'RECIPIENT');
                        window.location.href = 'profile_page.html';
                    } else if (userRole === 'BOTH') {
                        localStorage.setItem('userRole', 'BOTH');
                        localStorage.setItem('activeProfile', 'DONOR');
                        window.location.href = 'donor_dash.html';
                    } else {
                        window.location.href = 'index.html';
                    }
                } else {
                    messageDiv.textContent = 'Login failed: No token received.';
                    messageDiv.style.color = 'red';
                }
            } catch (error) {
                console.error('Login error:', error);
                const errorMessage = error.response && error.response.data ? error.response.data.message : 'An unexpected error occurred.';
                messageDiv.textContent = `Login failed: ${errorMessage}`;
                messageDiv.style.color = 'red';
            }
        });
    }

    if (registerForm) {
        const roleSelect = document.getElementById('role');
        const roleSpecificFieldsDiv = document.getElementById('roleSpecificFields');
        const donorFieldsDiv = document.getElementById('donorFields');

        roleSelect.addEventListener('change', () => {
            const selectedRole = roleSelect.value;
            if (selectedRole) {
                roleSpecificFieldsDiv.style.display = 'block';

                if (selectedRole === 'DONOR') {
                    donorFieldsDiv.style.display = 'block';
                } else {
                    donorFieldsDiv.style.display = 'none';
                }
            } else {
                roleSpecificFieldsDiv.style.display = 'none';
                donorFieldsDiv.style.display = 'none';
            }
        });

        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            messageDiv.textContent = ''; // Clear previous messages

            const fullName = document.getElementById('fullName').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const phone = document.getElementById('phone').value;
            const bloodType = document.getElementById('bloodType').value;
            const role = document.getElementById('role').value;

            const requestBody = {
                fullName,
                email,
                password,
                phone,
                bloodType,
                role
            };

            if (role === 'DONOR') {
                const lastDonationDate = document.getElementById('lastDonationDate').value;
                requestBody.lastDonationDate = lastDonationDate;
            }

            try {
                const response = await axios.post('/api/auth/register', requestBody);

                if (response.data && response.data.token) {
                    messageDiv.textContent = 'Registration successful! Redirecting to login...';
                    messageDiv.style.color = 'green';
                    // Optionally, log them in directly or redirect to login page
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 2000);
                } else {
                    messageDiv.textContent = 'Registration failed: No token received.';
                    messageDiv.style.color = 'red';
                }
            } catch (error) {
                console.error('Registration error:', error);
                const errorMessage = error.response && error.response.data ? error.response.data.message : 'An unexpected error occurred.';
                messageDiv.textContent = `Registration failed: ${errorMessage}`;
                messageDiv.style.color = 'red';
            }
        });
    }
});

function logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('recipientId');
    localStorage.removeItem('donorId');
    localStorage.removeItem('userRole');
    window.location.href = 'index.html';
}