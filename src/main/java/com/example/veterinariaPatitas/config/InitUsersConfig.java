package com.example.veterinariaPatitas.config;

import com.example.veterinariaPatitas.model.Usuario;
import com.example.veterinariaPatitas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitUsersConfig {

    @Bean
    public CommandLineRunner initUsers(UsuarioRepository usuarioRepository,
                                       PasswordEncoder encoder) {
        return args -> {

            // ===== ADMIN =====
            Usuario admin = usuarioRepository.findByUsuario("admin")
                    .orElse(new Usuario());

            admin.setUsuario("admin");
            admin.setContrasena(encoder.encode("123456"));
            admin.setRole("ADMIN");
            usuarioRepository.save(admin);
            System.out.println("ADMIN actualizado/creado ✔");

            // ===== TRABAJADOR =====
            Usuario trabajador = usuarioRepository.findByUsuario("trabajador")
                    .orElse(new Usuario());

            trabajador.setUsuario("trabajador");
            trabajador.setContrasena(encoder.encode("123456"));
            trabajador.setRole("TRABAJADOR");
            usuarioRepository.save(trabajador);
            System.out.println("TRABAJADOR actualizado/creado ✔");
        };
    }

}
