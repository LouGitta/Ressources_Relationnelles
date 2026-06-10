package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les utilisateurs.
 * Centralise la logique métier liée aux utilisateurs avec logging.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Récupère tous les utilisateurs.
     *
     * @return liste de tous les utilisateurs
     */
    public List<User> getAll() {
        logger.debug("Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur
     * @return Optional contenant l'utilisateur ou vide
     */
    public Optional<User> getById(Integer id) {
        logger.debug("Récupération de l'utilisateur {}", id);
        return userRepository.findById(id);
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email email de l'utilisateur
     * @return Optional contenant l'utilisateur ou vide
     */
    public Optional<User> getByEmail(String email) {
        logger.debug("Recherche d'utilisateur par email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     *
     * @param username nom d'utilisateur
     * @return Optional contenant l'utilisateur ou vide
     */
    public Optional<User> getByUsername(String username) {
        logger.debug("Recherche d'utilisateur par username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Sauvegarde un utilisateur.
     *
     * @param user l'utilisateur à sauvegarder
     * @return l'utilisateur sauvegardé
     */
    @Transactional
    public User save(User user) {
        logger.info("Sauvegarde de l'utilisateur: {} ({})", user.getUsername(), user.getId());
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur.
     *
     * @param id ID de l'utilisateur à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.warn("Suppression de l'utilisateur {}", id);
        userRepository.deleteById(id);
    }

    /**
     * Compte le nombre total d'utilisateurs.
     *
     * @return nombre d'utilisateurs
     */
    public long countAll() {
        logger.debug("Comptage total des utilisateurs");
        return userRepository.count();
    }

    public List<User> searchAndFilter(String keyword, Role role, Boolean isActive) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return userRepository.findWithFilters(keyword, role, isActive);
    }

    /**
     * Vérifie si un utilisateur a un rôle spécifique (ou plus élevé).
     *
     * @param userId ID de l'utilisateur
     * @param requiredRole Rôle requis
     * @return true si l'utilisateur a le rôle suffisant, false sinon
     */
    public boolean hasRole(Integer userId, Role requiredRole) {
        if (userId == null || requiredRole == null) {
            return false;
        }
        return getById(userId)
                .map(user -> cesi.RessourceRelationnelles.utils.AuthorizationHelper.hasRole(user, requiredRole))
                .orElse(false);
    }

    /**
     * Vérifie si un utilisateur est modérateur ou plus.
     *
     * @param userId ID de l'utilisateur
     * @return true si l'utilisateur est modérateur ou plus, false sinon
     */
    public boolean isModerator(Integer userId) {
        if (userId == null) {
            return false;
        }
        return getById(userId)
                .map(cesi.RessourceRelationnelles.utils.AuthorizationHelper::isModeratorOrAbove)
                .orElse(false);
    }
}