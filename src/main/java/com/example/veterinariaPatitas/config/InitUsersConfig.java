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

            // ===== TRABAJADOR PREDETERMINADO =====
            Usuario trabajador = usuarioRepository.findByUsuario("trabajador")
                    .orElse(new Usuario());

            trabajador.setUsuario("trabajador");
            trabajador.setContrasena(encoder.encode("123456"));
            trabajador.setRole("TRABAJADOR");
            usuarioRepository.save(trabajador);
            System.out.println("TRABAJADOR actualizado/creado ✔");

            // ===== CLIENTE DE PRUEBA (Opcional) =====
            Usuario clientePrueba = usuarioRepository.findByUsuario("cliente1")
                    .orElse(new Usuario());
            clientePrueba.setUsuario("cliente1");
            clientePrueba.setContrasena(encoder.encode("123456"));
            clientePrueba.setRole("CLIENTE"); 
            usuarioRepository.save(clientePrueba);
            System.out.println("CLIENTE de prueba actualizado/creado ✔");
        };
    }
}