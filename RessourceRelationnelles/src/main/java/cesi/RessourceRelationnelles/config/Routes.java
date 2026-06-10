package cesi.RessourceRelationnelles.config;

/**
 * Classe centralisée contenant toutes les routes de l'application.
 * Facilite le refactoring et évite le hardcoding des URLs.
 */
public final class Routes {

    private Routes() {
        // Classe utilitaire non instantiable
    }

    // ==================== PRÉFIXES ====================
    public static final String APP_PREFIX = "/app";
    public static final String API_PREFIX = "/api";

    // ==================== ROUTES PUBLIQUES ====================
    public static final String HOME = APP_PREFIX + "/home";
    public static final String LOGIN = APP_PREFIX + "/login";
    public static final String CREATE_ACCOUNT = APP_PREFIX + "/create-account";
    public static final String CGU = APP_PREFIX + "/cgu";
    public static final String LEGAL_MENTION = APP_PREFIX + "/legal";
    public static final String CONTACT = APP_PREFIX + "/contact";

    // ==================== ROUTES RESSOURCES ====================
    public static final String RESSOURCES = APP_PREFIX + "/ressources";
    public static final String RESSOURCE_DETAIL = APP_PREFIX + "/ressources/{id}";
    public static final String RESSOURCE_CREATE = APP_PREFIX + "/ressources/create";
    public static final String RESSOURCE_EDIT = APP_PREFIX + "/ressources/{id}/edit";
    public static final String RESSOURCE_DELETE = APP_PREFIX + "/ressources/{id}/delete";
    public static final String RESSOURCE_CATALOG = APP_PREFIX + "/ressources/catalog";

    // ==================== ROUTES COMMENTAIRES ====================
    public static final String ADD_COMMENT = APP_PREFIX + "/ressources/{id}/comments";
    public static final String DELETE_COMMENT = APP_PREFIX + "/comments/{id}/delete";
    public static final String EDIT_COMMENT = APP_PREFIX + "/comments/{id}/edit";

    // ==================== ROUTES PROFIL ====================
    public static final String PROFILE = APP_PREFIX + "/profile";
    public static final String ACCOUNT = APP_PREFIX + "/account";
    public static final String SETTINGS = APP_PREFIX + "/settings";

    // ==================== ROUTES AMIS ====================
    public static final String FRIENDS = APP_PREFIX + "/friends";
    public static final String ADD_FRIEND = APP_PREFIX + "/friends/add/{id}";
    public static final String REMOVE_FRIEND = APP_PREFIX + "/friends/remove/{id}";
    public static final String ACCEPT_FRIEND = APP_PREFIX + "/friends/accept/{id}";
    public static final String REJECT_FRIEND = APP_PREFIX + "/friends/reject/{id}";

    // ==================== ROUTES MODÉRATION ====================
    public static final String MODERATION = APP_PREFIX + "/ressources/moderation";
    public static final String ACCEPT_RESSOURCE = APP_PREFIX + "/ressources/moderation/{id}/accept";
    public static final String REJECT_RESSOURCE = APP_PREFIX + "/ressources/moderation/{id}/reject";

    // ==================== ROUTES FAVORIS ====================
    public static final String FAVORITES = APP_PREFIX + "/favorites";
    public static final String ADD_FAVORITE = APP_PREFIX + "/resources/{id}/favorite";
    public static final String REMOVE_FAVORITE = APP_PREFIX + "/resources/{id}/unfavorite";

    // ==================== ROUTES ADMIN ====================
    public static final String ADMIN_PANEL = APP_PREFIX + "/admin";
    public static final String ADMIN_USERS = APP_PREFIX + "/admin/users";
    public static final String ADMIN_RESSOURCES = APP_PREFIX + "/admin/ressources";

    // ==================== ROUTES PARTICIPATIONS (Activités) ====================
    public static final String PARTICIPATE = APP_PREFIX + "/resources/{id}/participate";
    public static final String LEAVE_ACTIVITY = APP_PREFIX + "/resources/{id}/leave";

    // ==================== ROUTES API REST ====================
    public static final String API_USERS = API_PREFIX + "/users";
    public static final String API_USER_DETAIL = API_PREFIX + "/users/{id}";
    public static final String API_RESSOURCES = API_PREFIX + "/ressources";
    public static final String API_RESSOURCE_DETAIL = API_PREFIX + "/ressources/{id}";
    public static final String API_COMMENTS = API_PREFIX + "/comments";
    public static final String API_COMMENT_DETAIL = API_PREFIX + "/comments/{id}";
    public static final String API_FRIENDS = API_PREFIX + "/friends";
    public static final String API_FAVORITES = API_PREFIX + "/favorites";

    // ==================== REDIRECTIONS COMMUNES ====================
    public static final String REDIRECT_HOME = "redirect:" + HOME;
    public static final String REDIRECT_LOGIN = "redirect:" + LOGIN;
    public static final String REDIRECT_PROFILE = "redirect:" + PROFILE;
    public static final String REDIRECT_RESSOURCES = "redirect:" + RESSOURCES;
    public static final String REDIRECT_MODERATION = "redirect:" + MODERATION;

}
