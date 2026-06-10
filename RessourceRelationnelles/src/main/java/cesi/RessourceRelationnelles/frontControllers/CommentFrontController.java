package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.dtos.CommentDTO;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Contrôleur pour gérer les opérations sur les commentaires.
 * Gère l'ajout et la suppression de commentaires.
 */
@Controller
public class CommentFrontController {

    private static final Logger logger = LoggerFactory.getLogger(CommentFrontController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private cesi.RessourceRelationnelles.services.PermissionService permissionService;

    // --- AJOUTER UN COMMENTAIRE ---
    /**
     * Ajoute un nouveau commentaire à une ressource.
     *
     * @param ressourceId L'ID de la ressource
     * @param content     Le contenu du commentaire
     * @param request     La requête HTTP
     * @param model       Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.ADD_COMMENT)
    public String addComment(
            @PathVariable("id") Integer ressourceId,
            @RequestParam("content") String content,
            HttpServletRequest request,
            Model model) {
        
        logger.info("Tentative d'ajout de commentaire pour la ressource ID: {}", ressourceId);
        
        // Récupérer l'utilisateur actuel
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié - redirection vers l'accueil");
            throw UnauthorizedException.notAuthenticated();
        }
        
        try {
            // Valider les entrées
            ValidationHelper.validateNotBlank(content, "contenu du commentaire");
            ValidationHelper.validatePositiveId(ressourceId, "ressourceId");
            
            // Créer et sauvegarder le commentaire via le Service métier
            commentService.createComment(ressourceId, content, user);
            logger.info("Commentaire créé avec succès pour la ressource: {}", ressourceId);
            
        } catch (IllegalArgumentException | ResourceNotFoundException | UnauthorizedException e) {
            logger.error("Erreur lors de l'ajout du commentaire: {}", e.getMessage());
            throw e;
        }
        
        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }

    // --- SUPPRIMER UN COMMENTAIRE ---
    /**
     * Supprime un commentaire si l'utilisateur est propriétaire ou modérateur.
     *
     * @param commentId    L'ID du commentaire à supprimer
     * @param ressourceId  L'ID de la ressource associée
     * @param request      La requête HTTP
     * @param model        Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.DELETE_COMMENT)
    public String deleteComment(
            @PathVariable("id") Integer commentId,
            @RequestParam("ressourceId") Integer ressourceId,
            HttpServletRequest request,
            Model model) {
        
        logger.info("Tentative de suppression du commentaire ID: {} de la ressource ID: {}", 
                    commentId, ressourceId);
        
        // Récupérer l'utilisateur actuel
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié - redirection vers l'accueil");
            throw UnauthorizedException.notAuthenticated();
        }
        
        try {
            // Valider les IDs
            ValidationHelper.validatePositiveId(commentId, "commentId");
            ValidationHelper.validatePositiveId(ressourceId, "ressourceId");
            
            // Vérifier les permissions via PermissionService
            if (!permissionService.canDeleteComment(user.getId(), commentId)) {
                logger.warn("Utilisateur {} tente de supprimer un commentaire non autorisé", user.getId());
                throw UnauthorizedException.forbidden("Supprimer ce commentaire");
            }
            
            // Supprimer le commentaire
            commentService.delete(commentId);
            logger.info("Commentaire {} supprimé avec succès par l'utilisateur {}", commentId, user.getId());
            
        } catch (IllegalArgumentException | ResourceNotFoundException | UnauthorizedException e) {
            logger.error("Erreur lors de la suppression du commentaire: {}", e.getMessage());
            throw e;
        }
        
        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }
}