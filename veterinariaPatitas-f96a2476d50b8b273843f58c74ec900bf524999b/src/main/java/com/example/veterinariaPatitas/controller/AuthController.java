package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Usuario;
import com.example.veterinariaPatitas.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {

        return usuarioRepository.findByUsuario(usuario)
                .filter(u -> u.getContrasena().equals(contrasena))
                .map(u -> {
                    session.setAttribute("usuarioLogeado", usuario);
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Usuario o contraseña incorrectos");
                    return "login";
                });
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String usuario,
                                   @RequestParam String contrasena,
                                   Model model) {

        if (usuarioRepository.findByUsuario(usuario).isPresent()) {
            model.addAttribute("error", "El usuario ya existe");
            return "registro";
        }

        Usuario nuevo = new Usuario(usuario, contrasena);
        usuarioRepository.save(nuevo);

        model.addAttribute("mensaje", "Usuario creado correctamente, ahora puedes iniciar sesión");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
