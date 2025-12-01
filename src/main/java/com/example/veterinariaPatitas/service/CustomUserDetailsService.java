package com.example.veterinariaPatitas.service;

import com.example.veterinariaPatitas.model.Usuario;
import com.example.veterinariaPatitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario u = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // ✅ CRÍTICO: Construcción robusta de la colección de autoridades
        // Asegura que solo se usa la cadena del rol de la base de datos ("TRABAJADOR")
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(u.getRole()));

        return User.builder()
                .username(u.getUsuario())
                .password(u.getContrasena())
                .authorities(authorities) 
                .build();
    }
}