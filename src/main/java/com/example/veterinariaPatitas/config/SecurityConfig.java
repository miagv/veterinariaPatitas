package com.example.veterinariaPatitas.config;

import com.example.veterinariaPatitas.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Asumimos que JwtFilter también ha sido corregido para no añadir "ROLE_"
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                // 1. ✅ CRÍTICO: Política de sesión para Thymeleaf
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) 
                )

                .authorizeHttpRequests(auth -> auth

                        // 2. ✅ CRÍTICO: Permite todos los estáticos y rutas públicas (¡Arregla el menú desarmado!)
                        .requestMatchers(
                                "/", "/login", "/logout",
                                "/css/**", "/js/**", "/img/**", "/favicon.ico",
                                "/sobre_nosotros", "/adoptar", "/envia_mensaje",
                                "/trabaja", "/upload", "/postular",
                                "/simulador_citas", "/book_appointment",
                                "/simulador_ventas", "/api/auth/login", "/api/auth/register"
                        ).permitAll()

                        // 3. ✅ CRÍTICO: Regla de acceso sin el prefijo ROLE_
                        .requestMatchers("/dashboard")
                        .hasAuthority("TRABAJADOR") // Debe coincidir con CustomUserDetailsService

                        // Rutas API protegidas
                        .requestMatchers("/api/**")
                        .authenticated()

                        .anyRequest().permitAll()
                )
                
                // 4. ✅ CRÍTICO: Configuración del Form Login para manejar el POST /login
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login") // URL donde se envían los datos del formulario
                    .usernameParameter("usuario") // Nombre del campo 'username' en tu login.html
                    .passwordParameter("contrasena") // Nombre del campo 'password' en tu login.html
                    .defaultSuccessUrl("/dashboard", true) // Redirección exitosa
                    .permitAll()
                )
                
                // Configuración de Logout
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                );

        // Añadir filtro JWT para APIs
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // Configuración de Beans para autenticación y encriptación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}