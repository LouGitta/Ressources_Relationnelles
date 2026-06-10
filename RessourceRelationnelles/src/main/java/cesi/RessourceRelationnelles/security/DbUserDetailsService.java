package cesi.RessourceRelationnelles.security;

import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service d'authentification Spring Security.
 * Recherche l'utilisateur d'abord par email (méthode principale),
 * puis par nom d'utilisateur en fallback pour la rétro-compatibilité.
 */
@Service
public class DbUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son email (identifiant principal de connexion).
     * En cas d'échec, tente une recherche par nom d'utilisateur.
     *
     * @param login L'email ou le nom d'utilisateur saisi dans le formulaire de connexion
     * @return Les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.debug("Tentative d'authentification pour : {}", login);

        // Recherche principale par email
        User u = userRepository.findByEmail(login)
                .orElseGet(() -> {
                    // Fallback par nom d'utilisateur
                    logger.debug("Aucun utilisateur trouvé par email '{}', tentative par username", login);
                    return userRepository.findByUsername(login)
                            .orElseThrow(() -> {
                                logger.warn("Authentification échouée — aucun utilisateur pour : {}", login);
                                return new UsernameNotFoundException("Utilisateur non trouvé : " + login);
                            });
                });

        logger.debug("Utilisateur '{}' trouvé (email: {})", u.getUsername(), u.getEmail());

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),        // username Spring Security = email (cohérence avec GlobalControllerAdvice)
                u.getPassword(),
                u.isActive(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
    }
}
