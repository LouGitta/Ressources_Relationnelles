package cesi.RessourceRelationnelles.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'application.
 * Gère toutes les exceptions et retourne des réponses d'erreur cohérentes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions ResourceNotFoundException.
     * Retourne une réponse 404 NOT FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        logger.warn("Ressource non trouvée: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions NoResourceFoundException (404 pour les ressources statiques manquantes).
     * Retourne une réponse 404 NOT FOUND au lieu d'une erreur 500.
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException ex,
            WebRequest request) {
        
        logger.warn("Ressource non trouvée (404): {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "La ressource demandée n'existe pas",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions UnauthorizedException.
     * Retourne une réponse 403 FORBIDDEN.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex,
            WebRequest request) {
        
        logger.warn("Accès refusé: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère les exceptions ValidationException.
     * Retourne une réponse 400 BAD REQUEST.
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        
        logger.warn("Erreur de validation: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        if (ex.getFieldName() != null) {
            errorResponse.setFieldName(ex.getFieldName());
        }
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions de validation JSR-303 (@Valid, @NotBlank, etc.).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        logger.warn("Erreurs de validation JSR-303 détectées");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
           .getFieldErrors()
           .forEach(error -> errors.put(
                   error.getField(),
                   error.getDefaultMessage()
           ));
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erreurs de validation",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        errorResponse.setValidationErrors(errors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions IllegalArgumentException.
     * Retourne une réponse 400 BAD REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        logger.warn("Argument invalide: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Argument invalide: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions inattendues.
     * Retourne une réponse 500 INTERNAL SERVER ERROR.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        logger.error("Erreur interne du serveur", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Une erreur interne du serveur s'est produite",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== CLASSE INTERNE ====================

    /**
     * Classe représentant une réponse d'erreur standardisée.
     */
    public static class ErrorResponse {

        private int status;
        private String message;
        private String path;
        private LocalDateTime timestamp;
        private String fieldName;
        private Map<String, String> validationErrors;

        public ErrorResponse(int status, String message, String path, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.path = path;
            this.timestamp = timestamp;
        }

        // ==================== GETTERS & SETTERS ====================

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Map<String, String> getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }
}
