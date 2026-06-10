package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Contrôleur pour gérer la participation aux activités/jeux.
 * Permet à un utilisateur de rejoindre et quitter une activité.
 * Délègue toutes les opérations sur les participants à {@link RessourceService}.
 */
@Controller
public class ParticipationFrontController {

    private static final Logger logger = LoggerFactory.getLogger(ParticipationFrontController.class);

    @Autowired
    private RessourceService ressourceService;

    /**
     * Permet à l'utilisateur de rejoindre une activité.
     *
     * @param id    ID de la ressource (activité)
     * @param model Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.RESSOURCE_DETAIL + "/join")
    public String joinActivity(@PathVariable Integer id, Model model) {
        logger.debug("Tentative de rejoindre l'activité {}", id);

        User currentUser = requireAuthenticated(model);
        ValidationHelper.validatePositiveId(id, "id");

        Ressource ressource = ressourceService.getById(id)
                .orElseThrow(() -> {
                    logger.warn("Ressource {} non trouvée", id);
                    return ResourceNotFoundException.notFound("Ressource", id);
                });

        ressourceService.joinActivity(ressource, currentUser);
        logger.info("L'utilisateur {} a rejoint l'activité {}", currentUser.getId(), id);

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", id.toString());
    }

    /**
     * Permet à l'utilisateur de quitter une activité.
     *
     * @param id    ID de la ressource (activité)
     * @param model Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.RESSOURCE_DETAIL + "/leave")
    public String leaveActivity(@PathVariable Integer id, Model model) {
        logger.debug("Tentative de quitter l'activité {}", id);

        User currentUser = requireAuthenticated(model);
        ValidationHelper.validatePositiveId(id, "id");

        ressourceService.leaveActivity(id, currentUser.getId());
        logger.info("L'utilisateur {} a quitté l'activité {}", currentUser.getId(), id);

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", id.toString());
    }

    /**
     * Vérifie que l'utilisateur courant est authentifié.
     *
     * @param model Le modèle Spring MVC
     * @return L'utilisateur courant
     * @throws UnauthorizedException si l'utilisateur n'est pas connecté
     */
    private User requireAuthenticated(Model model) {
        User user = (User) model.getAttribute("currentUser");
        if (user == null) {
            logger.warn("Opération participation tentée sans authentification");
            throw UnauthorizedException.notAuthenticated();
        }
        return user;
    }
}