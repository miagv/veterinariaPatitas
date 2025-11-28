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
        http
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                        
                        // Rutas Públicas (ESTÁTICOS Y PÁGINAS PRINCIPALES)
                        .requestMatchers(
                            "/", "/login", "/logout", 
                            "/css/**", "/js/**", "/img/**", "/favicon.ico", 
                            "/sobre_nosotros", "/adoptar", "/envia_mensaje", "/trabaja",
                            "/upload", "/postular"
                        ).permitAll()
                        
                        // Endpoints de Auth: /login y el NUEVO /register son públicos
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll() 
                        
                        // Rutas restringidas (necesitan token JWT)
                        .requestMatchers("/api/**").authenticated() 
                        
                        .anyRequest().permitAll()
            )
            
            // Se mantiene formLogin/logout para compatibilidad web, aunque el modal usa JWT
            .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("usuario")
                        .passwordParameter("contrasena")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
            )
            .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}