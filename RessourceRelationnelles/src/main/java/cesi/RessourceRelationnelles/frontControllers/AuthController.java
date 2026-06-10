package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour gérer l'authentification (page de login).
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Affiche la page de login.
     *
     * @return La vue "auth"
     */
    @GetMapping(Routes.LOGIN)
    public String afficherAuth() {
        logger.debug("Affichage de la page de login");
        return "auth";
    }
}

