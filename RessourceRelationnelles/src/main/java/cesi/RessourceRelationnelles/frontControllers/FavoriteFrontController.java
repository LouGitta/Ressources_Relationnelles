package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Contrôleur pour gérer les favoris.
 * Permet à un utilisateur connecté de marquer/démarquer une ressource en favori.
 */
@Controller
public class FavoriteFrontController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteFrontController.class);

    @Autowired
    private ProgressionService progressionService;

    /**
     * Bascule le statut favori d'une ressource pour l'utilisateur actuel.
     *
     * @param ressourceId ID de la ressource
     * @param request     La requête HTTP
     * @param model       Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.RESSOURCE_DETAIL + "/favorite")
    public String toggleFavorite(@PathVariable("id") Integer ressourceId, 
                                  HttpServletRequest request, 
                                  Model model) {
        logger.debug("Basculement du statut favori pour la ressource {}", ressourceId);

        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        // Validation
        ValidationHelper.validatePositiveId(ressourceId, "ressourceId");

        // Basculer le favori
        progressionService.toggleFavorite(user.getId(), ressourceId);

        logger.info("Statut favori de la ressource {} basculé pour l'utilisateur {}", 
            ressourceId, user.getId());

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }
}