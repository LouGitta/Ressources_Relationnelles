package cesi.RessourceRelationnelles.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC (non connecté)
                        .requestMatchers(
                                "/",                 // home
                                "/home",             // si tu as un endpoint home séparé
                                "/login",
                                "/register",
                                "/dsfr/**", "/js/**", "/images/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()

                        // SUPER ADMIN
                        .requestMatchers("/super-admin/**").hasRole("SUPER_ADMIN")

                        // ADMIN (admin + super admin)
                        .requestMatchers("/admin/**").hasAnyRole("ADMINISTRATOR", "SUPER_ADMIN")

                        // CONNECTÉ (citizen, moderator, admin, super_admin)
                        .requestMatchers("/app/**").authenticated()

                        // le reste: connecté (au début, c’est plus simple)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth")
                        .defaultSuccessUrl("/", true) // ou "/app" si tu préfères
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}