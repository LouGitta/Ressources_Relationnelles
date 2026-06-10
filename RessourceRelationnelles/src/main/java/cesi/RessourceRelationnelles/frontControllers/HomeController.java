package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Contrôleur pour la page d'accueil.
 * Affiche les ressources récentes.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RessourceService ressourceService;

    /**
     * Affiche la page d'accueil avec les ressources récentes.
     * GlobalControllerAdvice gère automatiquement: isConnected, isMobile, currentUser, isAdmin, isModo
     *
     * @param model   Le modèle pour la vue
     * @param request La requête HTTP
     * @return La vue "home"
     */
    @GetMapping(Routes.HOME)
    public String afficherHome(Model model, HttpServletRequest request) {
        logger.debug("Affichage de la page d'accueil");
        
        List<Ressource> recentRessources = ressourceService.getRecentRessources();
        model.addAttribute("recentRessources", recentRessources);
        
        logger.debug("Nombre de ressources récentes: {}", recentRessources.size());
        return "home";
    }
}
