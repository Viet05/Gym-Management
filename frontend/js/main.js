document.addEventListener('DOMContentLoaded', () => {
    // --- Login Logic ---
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            try {
                const response = await api.post('/auth/login', { username, password });

                if (response.code === 200 && response.data) {
                    const { token, role } = response.data; // Assuming response.data contains token and role
                    // If role is not directly in response, we might need to decode token or fetch profile.
                    // For now, let's assume the backend sends it or we decode it.
                    // If the backend only sends token, we'll store it.

                    localStorage.setItem('token', token);

                    // Simple role check (if backend returns it). 
                    // If not, we might need to parse the JWT payload.
                    // Let's try to parse JWT if role is missing in data but present in token
                    let userRole = role;
                    if (!userRole && token) {
                        try {
                            const payload = JSON.parse(atob(token.split('.')[1]));
                            userRole = payload.scope || payload.role; // Spring Security often puts roles in 'scope'
                        } catch (e) {
                            console.error('Error parsing token', e);
                        }
                    }

                    if (userRole && (userRole.includes('ADMIN') || userRole === 'ADMIN')) {
                        window.location.href = 'admin-dashboard.html';
                    } else {
                        // For normal users, maybe redirect to a user home or stay here (as per requirement "If admin... switch to dashboard")
                        // The requirement didn't specify what to do for normal users, but usually they have a home page.
                        // For now, alert or redirect to a placeholder.
                        alert('Login successful! (User interface not implemented yet)');
                    }
                } else {
                    alert('Login failed: ' + (response.message || 'Unknown error'));
                }
            } catch (error) {
                alert('Login error. Please check console.');
            }
        });
    }

    // --- Register Logic ---
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('reg-username').value;
            const password = document.getElementById('reg-password').value;
            const fullName = document.getElementById('reg-fullname').value;
            const email = document.getElementById('reg-email').value;
            const phone = document.getElementById('reg-phone').value;

            try {
                const response = await api.post('/auth/register', {
                    username, password, fullName, email, phone
                });

                if (response.code === 200) {
                    alert('Registration successful! Please login.');
                    window.location.href = 'login.html';
                } else {
                    alert('Registration failed: ' + (response.message || 'Unknown error'));
                }
            } catch (error) {
                alert('Registration error. Please check console.');
            }
        });
    }

    // --- Admin Dashboard Logic ---
    const userTableBody = document.getElementById('userTableBody');
    if (userTableBody) {
        loadUsers();

        // Logout
        document.getElementById('logoutBtn').addEventListener('click', () => {
            localStorage.removeItem('token');
            window.location.href = 'login.html';
        });

        // Form Submit (Create/Update)
        const userForm = document.getElementById('userForm');
        if (userForm) {
            userForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const id = document.getElementById('userId').value;
                const username = document.getElementById('user-username').value;
                const password = document.getElementById('user-password').value;
                const fullName = document.getElementById('user-fullname').value;
                const email = document.getElementById('user-email').value;
                const phone = document.getElementById('user-phone').value;
                const role = document.getElementById('user-role').value;
                const status = document.getElementById('user-status').value;

                try {
                    let response;
                    if (id) {
                        // Update: No username in payload
                        const data = { fullName, email, phone, role, status };
                        if (password) data.password = password;
                        response = await api.put(`/admin/dashboard/users/${id}`, data);
                    } else {
                        // Create: No status in payload (backend sets default)
                        const data = { userName: username, fullName, email, phone, role, password };
                        response = await api.post('/admin/dashboard/users', data);
                    }

                    if (response && response.code === 200) {
                        toggleUserForm(); // Close form
                        loadUsers();
                    } else {
                        alert('Operation failed: ' + (response.message || 'Unknown error'));
                    }
                } catch (error) {
                    console.error(error);
                    alert('Error saving user.');
                }
            });
        }
    }
});

// --- Helper Functions ---

async function loadUsers(params = {}) {
    try {
        // Build query string from params
        const queryString = new URLSearchParams(params).toString();
        const url = `/admin/dashboard/users${queryString ? '?' + queryString : ''}`;

        const response = await api.get(url);
        if (response.code === 200 && Array.isArray(response.data)) {
            renderUsers(response.data);
        }
    } catch (error) {
        console.error('Failed to load users', error);
    }
}

window.searchUsers = () => {
    const fullName = document.getElementById('search-fullname').value;
    const email = document.getElementById('search-email').value;
    loadUsers({ fullName, email });
};

window.resetSearch = () => {
    document.getElementById('search-fullname').value = '';
    document.getElementById('search-email').value = '';
    loadUsers();
};

function renderUsers(users) {
    const tbody = document.getElementById('userTableBody');
    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.userName}</td>
            <td>${user.fullName}</td>
            <td>${user.email}</td>
            <td><span class="status-badge" style="background: rgba(99, 102, 241, 0.2); color: #818cf8">${user.role}</span></td>
            <td><span class="status-badge ${user.status === 'ACTIVE' ? 'status-active' : 'status-inactive'}">${user.status}</span></td>
            <td>
                <button class="btn btn-sm btn-primary" onclick='editUser(${JSON.stringify(user)})'>Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

function toggleUserForm(user = null) {
    const formSection = document.getElementById('userFormSection');
    const formTitle = document.getElementById('formTitle');
    const form = document.getElementById('userForm');
    const toggleBtn = document.getElementById('toggleFormBtn');

    // Elements to toggle
    const usernameInput = document.getElementById('user-username');
    const statusGroup = document.getElementById('statusGroup');

    if (user) {
        // Edit Mode - hiển thị form với dữ liệu user
        formSection.classList.remove('hidden');
        formTitle.textContent = 'Chỉnh sửa User';
        document.getElementById('userId').value = user.id;

        usernameInput.value = user.userName;
        usernameInput.disabled = true; // Không cho sửa username

        document.getElementById('user-fullname').value = user.fullName;
        document.getElementById('user-email').value = user.email;
        document.getElementById('user-phone').value = user.phone || '';
        document.getElementById('user-role').value = user.role;
        document.getElementById('user-status').value = user.status;

        statusGroup.style.display = 'block'; // Hiển thị status khi edit
        document.getElementById('user-password').required = false; // Password không bắt buộc khi edit

        toggleBtn.textContent = 'Close Form';

        // Scroll to form
        formSection.scrollIntoView({ behavior: 'smooth' });
    } else {
        // Add Mode / Toggle
        if (formSection.classList.contains('hidden')) {
            // Hiển thị form để thêm user mới
            formSection.classList.remove('hidden');
            formTitle.textContent = 'Thêm User Mới';
            form.reset();
            document.getElementById('userId').value = '';
            usernameInput.disabled = false; // Cho phép nhập username
            statusGroup.style.display = 'none'; // Ẩn status khi tạo mới (backend tự set)
            document.getElementById('user-password').required = true; // Password bắt buộc khi tạo mới
            toggleBtn.textContent = 'Close Form';
        } else {
            // Ẩn form
            formSection.classList.add('hidden');
            form.reset();
            toggleBtn.textContent = '+ Add User';
        }
    }
}

// Expose to window for onclick handlers
window.editUser = (user) => toggleUserForm(user);
window.toggleUserForm = () => toggleUserForm(null);

window.deleteUser = async (id) => {
    if (confirm('Are you sure you want to delete this user?')) {
        try {
            await api.delete(`/admin/dashboard/users/${id}`);
            loadUsers();
        } catch (error) {
            alert('Failed to delete user.');
        }
    }
};
