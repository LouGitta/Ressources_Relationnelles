package cesi.RessourceRelationnelles.exceptions;

/**
 * Exception levée quand une ressource (User, Ressource, Comment, etc.) n'est pas trouvée.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Factory pour créer une exception avec un message standard.
     */
    public static ResourceNotFoundException notFound(String resourceType, Integer id) {
        return new ResourceNotFoundException(
                String.format("%s avec l'ID %d n'a pas été trouvé", resourceType, id)
        );
    }

    public static ResourceNotFoundException notFound(String resourceType, String identifier) {
        return new ResourceNotFoundException(
                String.format("%s '%s' n'a pas été trouvé", resourceType, identifier)
        );
    }
}
