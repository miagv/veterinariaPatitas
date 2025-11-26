document.addEventListener('DOMContentLoaded', function() {
    console.log("--- NAVBAR LOGIC INICIADA ---"); // DIAGNÓSTICO A: ¿Se ejecuta?

    const token = localStorage.getItem('jwtToken');
    const role = localStorage.getItem('jwtRole');

    const loginLink = document.getElementById('nav-login-link');
    const userMenu = document.getElementById('nav-user-menu');
    const logoutBtn = document.getElementById('logoutBtn');

    // DIAGNÓSTICO B: ¿Encuentra los elementos?
    console.log("Token JWT encontrado:", !!token);
    console.log("Enlace Login encontrado:", !!loginLink);
    console.log("Menú Usuario encontrado:", !!userMenu);

    // Personalización de la barra de navegación (Si hay token)
    if (token && userMenu && loginLink) {
        console.log("¡LOGEADO! Aplicando cambios al Navbar.");
        
        // Determinar el rol para el saludo
        const roleDisplay = role === 'ADMIN' ? 'Administrador' : 'Trabajador';
        
        // 1. Oculta el link de Login
        // ¡¡POSIBLE ERROR AQUÍ!!: Si el ID está en el <li>, debe ocultar el <li>
        if (loginLink.tagName === 'LI') {
             loginLink.classList.add('d-none');
        } else {
             // Si el ID está en el <a>, debemos ocultar el contenedor padre (<li>)
             // Esto requiere que el <a> esté dentro de un <li>
             loginLink.closest('li')?.classList.add('d-none');
        }


        // 2. Llenar el saludo en el menú desplegable
        const userDropdown = userMenu.querySelector('#userDropdown');
        if (userDropdown) {
            userDropdown.innerHTML = `👋 Hola, <b>${roleDisplay}</b>`; 
        }
        
        userMenu.classList.remove('d-none'); // 3. Muestra el menú de usuario logeado
    }

    // 4. Lógica para Cerrar Sesión (Necesaria para el botón)
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            // Eliminar datos del almacenamiento local
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('jwtUser');
            localStorage.removeItem('jwtRole');
            
            // Recargar la página para limpiar la vista
            location.reload(); 
        });
    }
});
   