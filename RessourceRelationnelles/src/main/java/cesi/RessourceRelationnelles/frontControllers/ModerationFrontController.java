package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Contrôleur pour les opérations de modération.
 * Permet aux modérateurs d'accepter ou rejeter les ressources en attente.
 */
@Controller
public class ModerationFrontController {

    private static final Logger logger = LoggerFactory.getLogger(ModerationFrontController.class);

    @Autowired
    private RessourceService ressourceService;

    /**
     * Affiche le panneau de modération avec toutes les ressources en attente.
     *
     * @param model   Le modèle pour la vue
     * @param request La requête HTTP
     * @return La vue "moderation"
     */
    @GetMapping(Routes.MODERATION)
    public String afficherModeration(Model model, HttpServletRequest request) {
        logger.debug("Accès au panneau de modération");
        
        // GlobalControllerAdvice gère automatiquement : isConnected, currentUser
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }
        
        // Vérifier que c'est un modérateur
        if (!AuthorizationHelper.isModeratorOrAbove(user)) {
            logger.warn("Utilisateur {} tente d'accéder au panneau de modération sans permissions", 
                       user.getId());
            throw UnauthorizedException.forbiddenRole("MODERATOR");
        }

        List<Ressource> pendingRessources = ressourceService.getPendingRessources();
        model.addAttribute("pendingRessources", pendingRessources);
        
        logger.info("Affichage de {} ressources en attente pour le modérateur {}", 
                   pendingRessources.size(), user.getId());

        return "moderation";
    }

    /**
     * Accepte une ressource (change son statut à "published").
     *
     * @param id      L'ID de la ressource
     * @param request La requête HTTP
     * @param model   Le modèle pour la vue
     * @return Redirection vers le panneau de modération
     */
    @PostMapping(Routes.ACCEPT_RESSOURCE)
    public String acceptRessource(
            @PathVariable Integer id,
            HttpServletRequest request,
            Model model) {
        
        logger.info("Tentative d'acceptation de la ressource ID: {}", id);
        
        // GlobalControllerAdvice gère automatiquement : currentUser
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié");
            throw UnauthorizedException.notAuthenticated();
        }
        
        try {
            // Vérifier les permissions
            if (!AuthorizationHelper.isModeratorOrAbove(user)) {
                logger.warn("Utilisateur {} tente d'accepter une ressource sans permissions", user.getId());
                throw UnauthorizedException.forbiddenRole("MODERATOR");
            }
            
            // Valider l'ID
            ValidationHelper.validatePositiveId(id, "ressourceId");
            
            // Mettre à jour le statut
            ressourceService.updateStatus(id, RessourceStatus.published);
            logger.info("Ressource {} acceptée et publiée par le modérateur {}", id, user.getId());
            
        } catch (IllegalArgumentException | UnauthorizedException e) {
            logger.error("Erreur lors de l'acceptation de la ressource: {}", e.getMessage());
            throw e;
        }
        
        return Routes.REDIRECT_MODERATION;
    }

    /**
     * Rejette une ressource (change son statut à "rejected").
     *
     * @param id      L'ID de la ressource
     * @param request La requête HTTP
     * @param model   Le modèle pour la vue
     * @return Redirection vers le panneau de modération
     */
    @PostMapping(Routes.REJECT_RESSOURCE)
    public String rejectRessource(
            @PathVariable Integer id,
            HttpServletRequest request,
            Model model) {
        
        logger.info("Tentative de rejet de la ressource ID: {}", id);
        
        // GlobalControllerAdvice gère automatiquement : currentUser
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié");
            throw UnauthorizedException.notAuthenticated();
        }
        
        try {
            // Vérifier les permissions
            if (!AuthorizationHelper.isModeratorOrAbove(user)) {
                logger.warn("Utilisateur {} tente de rejeter une ressource sans permissions", user.getId());
                throw UnauthorizedException.forbiddenRole("MODERATOR");
            }
            
            // Valider l'ID
            ValidationHelper.validatePositiveId(id, "ressourceId");
            
            // Mettre à jour le statut
            ressourceService.updateStatus(id, RessourceStatus.rejected);
            logger.info("Ressource {} rejetée par le modérateur {}", id, user.getId());
            
        } catch (IllegalArgumentException | UnauthorizedException e) {
            logger.error("Erreur lors du rejet de la ressource: {}", e.getMessage());
            throw e;
        }
        
        return Routes.REDIRECT_MODERATION;
    }
}
