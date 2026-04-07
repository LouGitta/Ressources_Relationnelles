package cesi.RessourceRelationnelles.security;

import cesi.RessourceRelationnelles.security.DbUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DbUserDetailsService userDetailsService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC (non connecté)
                        .requestMatchers(
                                "/static/dsfr/**", "/dsfr/**", "/dsfr/dsfr.module.min.js",
                                "/favicon.ico",
                                "/",
                                "/app/home",
                                "/app/ressources",
                                "/app/contact",
                                "/app/cgu",
                                "/app/legalMention",
                                "/app/create-account",
                                "/app/login", // ?
                                "/app/home",
                                "/login",
                                "/webjars/**"
                        ).permitAll()

                        // ADMIN (admin)
                        .requestMatchers("/admin/**",
                                "/app/profile"
                        ).hasRole("ADMIN")

                        // SUPER ADMIN (admin)
                        .requestMatchers("/admin/**",
                                "/app/profile"
                        ).hasRole("SUPER_ADMIN")

                        // MODERATOR
                        .requestMatchers("/app/ressources/moderation/**",
                                "/app/profile").hasRole("MODERATOR")

                        // CONNECTÉ citizen
                        .requestMatchers("/app/profile",
                                "/app/ressources/create", // !
                                "/app/").hasRole("CITIZEN") //!

                        // le reste: connecté (au début, c’est plus simple askip)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/app/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/app/home"))

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // Les API REST n'utilisent pas de session/CSRF
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:8100",  // Ionic dev
                "capacitor://localhost",  // Ionic sur appareil Android/iOS
                "ionic://localhost"       // Ionic sur appareil iOS (ancien)
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer à toutes les routes
        return source;
    }


}
