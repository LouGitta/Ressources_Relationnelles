package cesi.RessourceRelationnelles.utils;

/**
 * Classe utilitaire pour les validations communes.
 * Centralise la logique de validation réutilisable.
 */
public final class ValidationHelper {

    private ValidationHelper() {
        // Classe utilitaire non instantiable
    }

    /**
     * Valide qu'une chaîne de caractères n'est pas null ou vide.
     *
     * @param value La valeur à valider
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si la valeur est null ou vide
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être vide");
        }
    }

    /**
     * Valide la longueur d'une chaîne de caractères.
     *
     * @param value La valeur à valider
     * @param minLength La longueur minimale
     * @param maxLength La longueur maximale
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si la longueur n'est pas valide
     */
    public static void validateLength(String value, int minLength, int maxLength, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être null");
        }

        int length = value.trim().length();
        if (length < minLength || length > maxLength) {
            throw new IllegalArgumentException(
                    fieldName + " doit avoir entre " + minLength + " et " + maxLength + " caractères. " +
                    "Longueur actuelle: " + length);
        }
    }

    /**
     * Valide qu'un objet n'est pas null.
     *
     * @param value L'objet à valider
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si l'objet est null
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être null");
        }
    }

    /**
     * Valide qu'un ID est valide (positif).
     *
     * @param id L'ID à valider
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si l'ID n'est pas valide
     */
    public static void validatePositiveId(Integer id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " doit être un ID positif. Valeur: " + id);
        }
    }

    /**
     * Valide un pourcentage (0-100).
     *
     * @param percentage Le pourcentage à valider
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si le pourcentage n'est pas valide
     */
    public static void validatePercentage(Integer percentage, String fieldName) {
        if (percentage == null || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    fieldName + " doit être entre 0 et 100. Valeur: " + percentage);
        }
    }

    /**
     * Valide une adresse email (vérification basique).
     *
     * @param email L'email à valider
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si l'email n'est pas valide
     */
    public static void validateEmail(String email, String fieldName) {
        validateNotBlank(email, fieldName);

        // Vérification basique du format email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException(fieldName + " n'est pas une adresse email valide");
        }
    }

    /**
     * Supprime les espaces inutiles d'une chaîne.
     *
     * @param value La valeur à traiter
     * @return La chaîne trimée ou null si valeur null
     */
    public static String sanitizeInput(String value) {
        return value != null ? value.trim() : null;
    }

    /**
     * Vérifie qu'un nombre est dans une plage donnée.
     *
     * @param value La valeur à valider
     * @param min La valeur minimale (inclusive)
     * @param max La valeur maximale (inclusive)
     * @param fieldName Le nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si la valeur n'est pas dans la plage
     */
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    fieldName + " doit être entre " + min + " et " + max + ". Valeur: " + value);
        }
    }

}
