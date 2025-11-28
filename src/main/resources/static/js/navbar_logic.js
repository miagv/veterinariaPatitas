document.addEventListener('DOMContentLoaded', function() {
    
    // 1. Inicializar Modales (Requiere Bootstrap JS)
    const mainModalEl = document.getElementById('mainLoginModal');
    const clienteModalEl = document.getElementById('clienteLoginModal');
    const trabajadorModalEl = document.getElementById('trabajadorLoginModal');

    const mainModal = mainModalEl ? new bootstrap.Modal(mainModalEl) : null;
    const clienteModal = clienteModalEl ? new bootstrap.Modal(clienteModalEl) : null;
    const trabajadorModal = trabajadorModalEl ? new bootstrap.Modal(trabajadorModalEl) : null;

    // Elementos de Navbar
    const loginLink = document.getElementById('nav-login-link');
    const userMenu = document.getElementById('nav-user-menu'); 
    const logoutBtn = document.getElementById('logoutBtn');

    // 2. Manejo de Selección de Rol (Desde Modal Principal)
    if (loginLink) {
        // Asegurar que el enlace de Login abra el modal principal
        loginLink.addEventListener('click', function(e) {
            e.preventDefault();
            if (mainModal) mainModal.show();
        });
    }

    const btnCliente = document.getElementById('btnSoyCliente');
    const btnTrabajador = document.getElementById('btnSoyTrabajador');

    if (btnCliente) {
        btnCliente.addEventListener('click', () => {
            if (mainModal) mainModal.hide();
            if (clienteModal) clienteModal.show();
        });
    }

    if (btnTrabajador) {
        btnTrabajador.addEventListener('click', () => {
            if (mainModal) mainModal.hide();
            if (trabajadorModal) trabajadorModal.show();
        });
    }

    // 3. Lógica de Vistas dentro de Modal Cliente (Login <-> Registro)
    const showRegisterLink = document.getElementById('showRegisterLink');
    const showLoginLink = document.getElementById('showLoginLink');
    const clienteLoginView = document.getElementById('clienteLoginView');
    const clienteRegisterView = document.getElementById('clienteRegisterView');
    const clienteModalTitle = clienteModalEl ? clienteModalEl.querySelector('.modal-title') : null;


    if (showRegisterLink && clienteLoginView && clienteRegisterView && clienteModalTitle) {
        showRegisterLink.addEventListener('click', (e) => {
            e.preventDefault();
            clienteLoginView.style.display = 'none';
            clienteRegisterView.style.display = 'block';
            clienteModalTitle.innerText = 'Registro de Cliente';
        });

        showLoginLink.addEventListener('click', (e) => {
            e.preventDefault();
            clienteLoginView.style.display = 'block';
            clienteRegisterView.style.display = 'none';
            clienteModalTitle.innerText = 'Acceso Cliente';
        });
    }


    // 4. Lógica de Login (Compartida para Cliente y Trabajador)
    async function handleLogin(usernameId, passwordId, errorId, modalInstance) {
        const user = document.getElementById(usernameId).value;
        const pass = document.getElementById(passwordId).value;
        const errorDisplay = document.getElementById(errorId);
        
        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {'Content-Type':'application/json'},
                body: JSON.stringify({username: user, password: pass})
            });
            const data = await res.json();
            
            if (!res.ok) {
                errorDisplay.innerText = data.error || 'Credenciales incorrectas.';
                errorDisplay.style.display = 'block';
                return;
            }
            
            // Éxito: Guardar datos y redirigir
            localStorage.setItem('jwtToken', data.token);
            localStorage.setItem('jwtRole', data.role);
            localStorage.setItem('jwtUser', data.username);
            
            modalInstance.hide();
            location.href = "/"; // Redirección al dashboard (ruta protegida)
            
        } catch (err) {
            console.error(err);
            errorDisplay.innerText = 'Error de conexión con el servidor.';
            errorDisplay.style.display = 'block';
        }
    }

    if (document.getElementById('clienteLoginForm')) {
        document.getElementById('clienteLoginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            handleLogin('clienteUsername', 'clientePassword', 'clienteLoginError', clienteModal);
        });
    }

    if (document.getElementById('trabajadorLoginForm')) {
        document.getElementById('trabajadorLoginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            handleLogin('trabajadorUsername', 'trabajadorPassword', 'trabajadorLoginError', trabajadorModal);
        });
    }


    // 5. Lógica de Registro (Solo Clientes)
    if (document.getElementById('clienteRegisterForm')) {
        document.getElementById('clienteRegisterForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            const user = document.getElementById('registerUsername').value;
            const pass = document.getElementById('registerPassword').value;
            const errorDisplay = document.getElementById('clienteRegisterError');

            try {
                const res = await fetch('/api/auth/register', { 
                    method: 'POST',
                    headers: {'Content-Type':'application/json'},
                    body: JSON.stringify({username: user, password: pass})
                });
                const data = await res.json();
                
                if (!res.ok) {
                    errorDisplay.innerText = data.error || 'Error al registrar el usuario.';
                    errorDisplay.style.display = 'block';
                    return;
                }
                
                // Éxito: Logeado automáticamente y redirigiendo
                localStorage.setItem('jwtToken', data.token);
                localStorage.setItem('jwtRole', data.role);
                localStorage.setItem('jwtUser', data.username);
                
                clienteModal.hide();
                location.href = "/"; 
                
            } catch (err) {
                console.error(err);
                errorDisplay.innerText = 'Error de conexión al intentar registrar.';
                errorDisplay.style.display = 'block';
            }
        });
    }

    // 6. Lógica de Navbar y Logout 
    const token = localStorage.getItem('jwtToken');
    const role = localStorage.getItem('jwtRole');
    
    // Personalización de la barra de navegación (Si hay token)
    if (token && userMenu && loginLink) {
        
        let roleDisplay = '';
        if (role === 'CLIENTE') {
            roleDisplay = 'Cliente';
        } else if (role === 'TRABAJADOR') {
            roleDisplay = 'Trabajador';
        }

        // Oculta el link de Login
        loginLink.closest('li')?.classList.add('d-none');
        
        // Llenar el saludo en el menú desplegable
        const userDropdown = userMenu.querySelector('#userDropdown');
        if (userDropdown) {
            userDropdown.innerHTML = `👋 Hola, <b>${roleDisplay}</b>`; 
        }
        
        userMenu.classList.remove('d-none'); // Muestra el menú de usuario logeado
    }

    // Lógica para Cerrar Sesión
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('jwtUser');
            localStorage.removeItem('jwtRole');
            
            location.href = "/"; // Volver a la página principal (deslogeado)
        });
    }
});