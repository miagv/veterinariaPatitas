package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Usuario;
import com.example.veterinariaPatitas.repository.UsuarioRepository;
import com.example.veterinariaPatitas.security.JwtUtil;
import com.example.veterinariaPatitas.security.payload.LoginRequest; 
import com.example.veterinariaPatitas.security.payload.RegisterRequest; // NUEVO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder; // NUEVO
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired 
    private PasswordEncoder passwordEncoder; // NUEVO

    // =======================================================
    // MÉTODO DE LOGIN (Se mantiene igual, maneja CLIENTE y TRABAJADOR)
    // =======================================================
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        try {
            // Intenta autenticar al usuario (usa CustomUserDetailsService y PasswordEncoder)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Si es exitoso, busca el rol
            Optional<Usuario> userOptional = usuarioRepository.findByUsuario(loginRequest.getUsername());
            String role = userOptional.isPresent() ? userOptional.get().getRole() : "USER";

            // Genera y devuelve el token JWT
            String jwt = jwtUtil.generateToken(loginRequest.getUsername(), role);

            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "username", loginRequest.getUsername(),
                    "role", role
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas."));
        }
    }
    
    // =======================================================
    // NUEVO ENDPOINT DE REGISTRO (Solo para CLIENTES)
    // =======================================================
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        
        if (usuarioRepository.existsByUsuario(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "El nombre de usuario ya está en uso."));
        }
        
        // Crea y guarda el nuevo usuario con rol CLIENTE
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsuario(registerRequest.getUsername());
        nuevoUsuario.setContrasena(passwordEncoder.encode(registerRequest.getPassword()));
        nuevoUsuario.setRole("CLIENTE"); // Rol fijo para nuevos registros
        
        usuarioRepository.save(nuevoUsuario);
        
        // Logea automáticamente al cliente recién registrado
        String jwt = jwtUtil.generateToken(nuevoUsuario.getUsuario(), nuevoUsuario.getRole());
        
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "username", nuevoUsuario.getUsuario(),
                "role", nuevoUsuario.getRole(),
                "message", "Registro exitoso."
        ));
    }
}