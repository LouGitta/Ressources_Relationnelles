package cesi.RessourceRelationnelles.config;

/**
 * Classe centralisée contenant toutes les constantes de l'application.
 * Évite le hardcoding et facilite les modifications globales.
 */
public final class AppConstants {

    private AppConstants() {
        // Classe utilitaire non instantiable
    }

    // ==================== MESSAGES D'ERREUR ====================
    public static final String ERROR_USER_NOT_FOUND = "Utilisateur non trouvé";
    public static final String ERROR_RESSOURCE_NOT_FOUND = "Ressource non trouvée";
    public static final String ERROR_COMMENT_NOT_FOUND = "Commentaire non trouvé";
    public static final String ERROR_FRIEND_NOT_FOUND = "Ami non trouvé";
    public static final String ERROR_USER_NOT_AUTHENTICATED = "Utilisateur non authentifié";
    public static final String ERROR_UNAUTHORIZED_ACTION = "Action non autorisée";
    public static final String ERROR_INVALID_INPUT = "Entrée invalide";
    public static final String ERROR_RESOURCE_ALREADY_EXISTS = "La ressource existe déjà";
    public static final String ERROR_INVALID_ROLE = "Rôle invalide";

    // ==================== MESSAGES DE SUCCÈS ====================
    public static final String SUCCESS_COMMENT_CREATED = "Commentaire créé avec succès";
    public static final String SUCCESS_COMMENT_DELETED = "Commentaire supprimé avec succès";
    public static final String SUCCESS_RESSOURCE_CREATED = "Ressource créée avec succès";
    public static final String SUCCESS_RESSOURCE_UPDATED = "Ressource mise à jour avec succès";
    public static final String SUCCESS_RESSOURCE_DELETED = "Ressource supprimée avec succès";
    public static final String SUCCESS_ACTION_COMPLETED = "Action complétée avec succès";

    // ==================== VALIDATIONS ====================
    public static final int TITLE_MIN_LENGTH = 3;
    public static final int TITLE_MAX_LENGTH = 255;
    public static final int CONTENT_MIN_LENGTH = 10;
    public static final int CONTENT_MAX_LENGTH = 10000;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 50;
    public static final int EMAIL_MAX_LENGTH = 255;
    public static final int PASSWORD_MIN_LENGTH = 8;

    // ==================== PARAMÈTRES D'APPLICATION ====================
    public static final boolean DEVELOPMENT_MODE = Boolean.parseBoolean(
            System.getenv("DEV_MODE") != null ? System.getenv("DEV_MODE") : "false"
    );
    
    public static final Integer DEFAULT_PAGINATION_SIZE = 20;
    public static final Integer MAX_PAGINATION_SIZE = 100;

    // ==================== DURÉES (en millisecondes) ====================
    public static final long SESSION_TIMEOUT = 3600000; // 1 heure
    public static final long TOKEN_EXPIRATION = 86400000; // 24 heures

}
