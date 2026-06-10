package cesi.RessourceRelationnelles.utils;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;

/**
 * Classe utilitaire pour centraliser la logique d'autorisation.
 * Évite la duplication des vérifications de permission dans les contrôleurs.
 */
public final class AuthorizationHelper {

    private AuthorizationHelper() {
        // Classe utilitaire non instantiable
    }

    /**
     * Vérifie si un utilisateur est modérateur ou administrateur.
     *
     * @param user L'utilisateur à vérifier
     * @return true si l'utilisateur a des droits de modération
     */
    public static boolean isModeratorOrAbove(User user) {
        if (user == null) {
            return false;
        }
        return user.getRole() == Role.MODERATOR ||
               user.getRole() == Role.ADMINISTRATOR ||
               user.getRole() == Role.SUPERADMIN;
    }

    /**
     * Vérifie si un utilisateur est administrateur ou superadministrateur.
     *
     * @param user L'utilisateur à vérifier
     * @return true si l'utilisateur a des droits d'administration
     */
    public static boolean isAdministratorOrAbove(User user) {
        if (user == null) {
            return false;
        }
        return user.getRole() == Role.ADMINISTRATOR ||
               user.getRole() == Role.SUPERADMIN;
    }

    /**
     * Vérifie si un utilisateur est superadministrateur.
     *
     * @param user L'utilisateur à vérifier
     * @return true si l'utilisateur est superadmin
     */
    public static boolean isSuperAdmin(User user) {
        if (user == null) {
            return false;
        }
        return user.getRole() == Role.SUPERADMIN;
    }

    /**
     * Vérifie si un utilisateur est un citoyen (rôle minimal).
     *
     * @param user L'utilisateur à vérifier
     * @return true si l'utilisateur est citoyen
     */
    public static boolean isCitizen(User user) {
        if (user == null) {
            return false;
        }
        return user.getRole() == Role.CITIZEN;
    }

    /**
     * Vérifie si l'utilisateur actif a au moins le rôle spécifié.
     *
     * @param user L'utilisateur à vérifier
     * @param requiredRole Le rôle minimum requis
     * @return true si l'utilisateur a le rôle suffisant
     */
    public static boolean hasRole(User user, Role requiredRole) {
        if (user == null) {
            return false;
        }

        int userLevel = getRoleLevel(user.getRole());
        int requiredLevel = getRoleLevel(requiredRole);

        return userLevel >= requiredLevel;
    }

    /**
     * Retourne le niveau hiérarchique d'un rôle.
     * CITIZEN < MODERATOR < ADMINISTRATOR < SUPERADMIN
     *
     * @param role Le rôle à évaluer
     * @return Le niveau hiérarchique du rôle
     */
    private static int getRoleLevel(Role role) {
        switch (role) {
            case CITIZEN:
                return 0;
            case MODERATOR:
                return 1;
            case ADMINISTRATOR:
                return 2;
            case SUPERADMIN:
                return 3;
            default:
                return -1;
        }
    }

    /**
     * Vérifie si un utilisateur est actif et authentifié.
     *
     * @param user L'utilisateur à vérifier
     * @return true si l'utilisateur est actif
     */
    public static boolean isUserActive(User user) {
        return user != null && user.isActive();
    }

    /**
     * Vérifie si deux IDs utilisateur correspondent (même utilisateur).
     *
     * @param userId1 Premier ID utilisateur
     * @param userId2 Deuxième ID utilisateur
     * @return true si les IDs correspondent
     */
    public static boolean isSameUser(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null) {
            return false;
        }
        return userId1.equals(userId2);
    }

}
