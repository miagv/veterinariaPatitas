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

        return User.builder()
                .username(u.getUsuario())
                .password(u.getContrasena())
                .authorities("ROLE_" + u.getRole()) // ADMIN o TRABAJADOR
                .build();
    }
}
