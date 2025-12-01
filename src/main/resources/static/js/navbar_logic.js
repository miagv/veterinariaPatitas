document.addEventListener('DOMContentLoaded', function () {

    // ========== MODALES PARA LOGIN ==========
    const mainModalEl = document.getElementById('mainLoginModal');
    const clienteModalEl = document.getElementById('clienteLoginModal');
    const trabajadorModalEl = document.getElementById('trabajadorLoginModal');

    const mainModal = mainModalEl ? new bootstrap.Modal(mainModalEl) : null;
    const clienteModal = clienteModalEl ? new bootstrap.Modal(clienteModalEl) : null;
    const trabajadorModal = trabajadorModalEl ? new bootstrap.Modal(trabajadorModalEl) : null;

    // Navbar items
    const loginLink = document.getElementById('nav-login-link');
    const userMenu = document.getElementById('nav-user-menu');
    const logoutBtn = document.getElementById('logoutBtn');

    // ========== BOTÓN LOGIN ABRE MODAL DE ROLES ==========
    if (loginLink) {
        loginLink.addEventListener('click', function (e) {
            e.preventDefault();
            if (mainModal) mainModal.show();
        });
    }

    // ========== SELECCIÓN DE ROL ==========
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

    // ========== INTERCAMBIO LOGIN/REGISTRO EN CLIENTE ==========
    const showRegisterLink = document.getElementById('showRegisterLink');
    const showLoginLink = document.getElementById('showLoginLink');
    const clienteLoginView = document.getElementById('clienteLoginView');
    const clienteRegisterView = document.getElementById('clienteRegisterView');
    const clienteModalTitle = clienteModalEl ? clienteModalEl.querySelector('.modal-title') : null;

    if (showRegisterLink && showLoginLink && clienteLoginView && clienteRegisterView) {

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

    // ================================================================
    // ========== LOGIN PARA CLIENTE Y TRABAJADOR ==========
    // ================================================================
    async function handleLogin(usernameId, passwordId, errorId, modalInstance) {
        const user = document.getElementById(usernameId).value;
        const pass = document.getElementById(passwordId).value;
        const errorDisplay = document.getElementById(errorId);

        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: user, password: pass })
            });

            const data = await res.json();

            if (!res.ok) {
                errorDisplay.innerText = data.error || 'Credenciales incorrectas.';
                errorDisplay.style.display = 'block';
                return;
            }

            // Guardar JWT
            localStorage.setItem('jwtToken', data.token);
            localStorage.setItem('jwtRole', data.role);
            localStorage.setItem('jwtUser', data.username);

            modalInstance.hide();
            location.reload();

        } catch (err) {
            console.error(err);
            errorDisplay.innerText = 'Error de conexión con el servidor.';
            errorDisplay.style.display = 'block';
        }
    }

    // Forms
    if (document.getElementById('clienteLoginForm')) {
        document.getElementById('clienteLoginForm').addEventListener('submit', function (e) {
            e.preventDefault();
            handleLogin('clienteUsername', 'clientePassword', 'clienteLoginError', clienteModal);
        });
    }

    if (document.getElementById('trabajadorLoginForm')) {
        document.getElementById('trabajadorLoginForm').addEventListener('submit', function (e) {
            e.preventDefault();
            handleLogin('trabajadorUsername', 'trabajadorPassword', 'trabajadorLoginError', trabajadorModal);
        });
    }

    // ================================================================
    // ========== REGISTRO DE CLIENTE ==========
    // ================================================================
    if (document.getElementById('clienteRegisterForm')) {

        document.getElementById('clienteRegisterForm').addEventListener('submit', async function (e) {

            e.preventDefault();

            const user = document.getElementById('registerUsername').value;
            const pass = document.getElementById('registerPassword').value;
            const errorDisplay = document.getElementById('clienteRegisterError');

            try {
                const res = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username: user, password: pass })
                });

                const data = await res.json();

                if (!res.ok) {
                    errorDisplay.innerText = data.error || 'Error al registrar el usuario.';
                    errorDisplay.style.display = 'block';
                    return;
                }

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

    // ================================================================
    // ========== VISUALIZAR MENÚ SEGÚN ROL (CORREGIDO) ==========
    // ================================================================
    const token = localStorage.getItem('jwtToken');
    const role = localStorage.getItem('jwtRole');

    const loginItem = document.getElementById('nav-login-link');
    const userMenuItem = document.getElementById('nav-user-menu');

    const allNavItems = document.querySelectorAll(".nav-item:not(#nav-user-menu):not(#nav-login-link)");

    const dashboardItem =
        document.querySelector("a.dropdown-item[href='/dashboard']")?.parentElement;

    

    if (!token) {
        loginItem?.classList.remove("d-none");
        userMenuItem?.classList.add("d-none");

        allNavItems.forEach(i => i.style.display = "block");

        if (dashboardItem) dashboardItem.style.display = "none";
        
    }
    else if (role === "CLIENTE") {
        loginItem?.classList.add("d-none");
        userMenuItem?.classList.remove("d-none");

        allNavItems.forEach(i => i.style.display = "block");

        if (dashboardItem) dashboardItem.style.display = "none";
      

        const userDropdown = userMenuItem.querySelector('#userDropdown');
        if (userDropdown) userDropdown.innerHTML = "👋 Hola, <b>Cliente</b>";
    }
    else if (role === "TRABAJADOR") {
        loginItem?.classList.add("d-none");
        userMenuItem?.classList.remove("d-none");

        allNavItems.forEach(item => {
            const href = item.querySelector("a")?.getAttribute("href");
            if (href === "/dashboard") {
                item.style.display = "block";
            } else {
                item.style.display = "none";
            }
        });

        if (dashboardItem) dashboardItem.style.display = "block";
        

        const userDropdown = userMenuItem.querySelector('#userDropdown');
        if (userDropdown) userDropdown.innerHTML = "👋 Hola, <b>Trabajador</b>";
    }

    // ================================================================
    // ========== LOGOUT ==========
    // ================================================================
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function (e) {
            e.preventDefault();

            localStorage.removeItem('jwtToken');
            localStorage.removeItem('jwtRole');
            localStorage.removeItem('jwtUser');

            location.href = "/";
        });
    }

});

