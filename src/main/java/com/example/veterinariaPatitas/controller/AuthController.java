package com.example.veterinariaPatitas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String usuario,
            @RequestParam String contrasena,
            @RequestParam String rol,
            HttpSession session,
            Model model) {

        // ---- USUARIO ADMIN PREDETERMINADO ----
        if (usuario.equals("admin") && contrasena.equals("admin123") && rol.equals("ADMIN")) {
            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", rol);
            return "redirect:/dashboard";
        }

        // ---- USUARIO TRABAJADOR EJEMPLO ----
        if (usuario.equals("trabajador") && contrasena.equals("123456") && rol.equals("TRABAJADOR")) {
            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", rol);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Crea dashboard.html si no lo tienes
    }
}
