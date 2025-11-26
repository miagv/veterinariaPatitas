package com.example.veterinariaPatitas.controller;


import com.example.veterinariaPatitas.model.Usuario;
import com.example.veterinariaPatitas.repository.UsuarioRepository;
import com.example.veterinariaPatitas.security.JwtUtil;
import com.example.veterinariaPatitas.security.payload.LoginRequest; // Usa el paquete que creaste
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        try {
            // 1. Intenta autenticar al usuario usando el AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Si es exitoso, busca el rol en la base de datos
            Optional<Usuario> userOptional = usuarioRepository.findByUsuario(loginRequest.getUsername());
            String role = userOptional.isPresent() ? userOptional.get().getRole() : "USER";

            // 3. Generar el Token JWT
            String jwt = jwtUtil.generateToken(loginRequest.getUsername(), role);

            // 4. Devolver el token y los datos
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "username", loginRequest.getUsername(),
                    "role", role
            ));

        } catch (Exception e) {
            // Manejar errores de autenticación
            // NOTA: Aquí atrapa cualquier error, incluyendo UsernameNotFoundException o BadCredentialsException
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas."));
        }
    }
}