package com.example.veterinariaPatitas.config;

import com.example.veterinariaPatitas.security.JwtFilter; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // 👈 NUEVO: Importación para PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 

// 👈 PASO 1: HABILITAR LA SEGURIDAD A NIVEL DE MÉTODO
@Configuration
@EnableMethodSecurity(securedEnabled = true) // Habilita @PreAuthorize y @Secured
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
 "/upload", "/postular",
                            "/simulador_citas", "/book_appointment" // 👈 Rutas de cliente/citas públicas
 ).permitAll()
 // Endpoints de Auth: /login y /register son públicos
 .requestMatchers("/api/auth/login", "/api/auth/register").permitAll() 
 
                        // PASO 2: Permite el acceso a todas las rutas /api/** a usuarios AUTENTICADOS.
                        // La restricción de ROL (TRABAJADOR) se aplica en el controlador con @PreAuthorize.
 .requestMatchers("/api/**").authenticated() 
 .anyRequest().permitAll()
)
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