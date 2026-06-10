package cesi.RessourceRelationnelles.exceptions;

/**
 * Exception levée quand un utilisateur n'a pas les permissions pour effectuer une action.
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnauthorizedException notAuthenticated() {
        return new UnauthorizedException("Vous devez être authentifié pour effectuer cette action");
    }

    public static UnauthorizedException forbidden(String action) {
        return new UnauthorizedException(
                String.format("Vous n'avez pas la permission d'effectuer cette action: %s", action)
        );
    }

    public static UnauthorizedException forbiddenRole(String requiredRole) {
        return new UnauthorizedException(
                String.format("Cette action nécessite le rôle: %s", requiredRole)
        );
    }
}
