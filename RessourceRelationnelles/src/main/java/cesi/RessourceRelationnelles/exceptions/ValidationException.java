package cesi.RessourceRelationnelles.exceptions;

/**
 * Exception levée quand les données validées ne satisfont pas les critères attendus.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String fieldName;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getFieldName() {
        return fieldName;
    }

    public static ValidationException invalidField(String fieldName, String reason) {
        return new ValidationException(
                String.format("Champ invalide '%s': %s", fieldName, reason),
                fieldName
        );
    }

    public static ValidationException emptyField(String fieldName) {
        return new ValidationException(
                String.format("Le champ '%s' ne peut pas être vide", fieldName),
                fieldName
        );
    }

    public static ValidationException invalidLength(String fieldName, int minLength, int maxLength, int actualLength) {
        return new ValidationException(
                String.format("Le champ '%s' doit avoir entre %d et %d caractères (actuellement: %d)",
                        fieldName, minLength, maxLength, actualLength),
                fieldName
        );
    }
}
