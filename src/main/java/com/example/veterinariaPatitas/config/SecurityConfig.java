package com.example.veterinariaPatitas.config;


import com.example.veterinariaPatitas.security.JwtFilter; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 

@Configuration
public class SecurityConfig {
    
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 🚨 CAMBIO CRÍTICO: Usa el método 'authorizeHttpRequests' para configurar las reglas 🚨
        http
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 1. Configurar las reglas de acceso
            .authorizeHttpRequests(auth -> auth
                    
                    // Rutas Públicas (ESTÁTICOS Y PÁGINAS PRINCIPALES)
                    .requestMatchers(
                        "/", "/login", "/logout", 
                        "/css/**", "/js/**", "/img/**", "/favicon.ico", // <-- Estos ya estaban, pero los mantenemos
                        "/sobre_nosotros", "/adoptar", "/envia_mensaje", "/trabaja",
                        "/upload", "/postular"
                    ).permitAll()
                    
                    // Endpoint de generación de token (PÚBLICO)
                    .requestMatchers("/api/auth/login").permitAll() 
                    
                    // Rutas restringidas (necesitan token JWT)
                    .requestMatchers("/api/**", "/dashboard/**").authenticated() 
                    
                    // Cualquier otra petición que no coincida es pública
                    .anyRequest().permitAll()
            )
            
            // 2. Configuración de Login y Logout (Base de sesión, aunque priorizamos JWT)
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("usuario")
                    .passwordParameter("contrasena")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .permitAll()
            );

        // 3. INTEGRACIÓN DEL FILTRO JWT (DEBE IR AL FINAL)
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}