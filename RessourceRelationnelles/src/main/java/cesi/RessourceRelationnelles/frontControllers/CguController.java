package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour afficher les conditions générales d'utilisation.
 */
@Controller
public class CguController {

    private static final Logger logger = LoggerFactory.getLogger(CguController.class);

    /**
     * Affiche la page CGU.
     *
     * @return La vue "cgu"
     */
    @GetMapping(Routes.CGU)
    public String afficherCgu() {
        logger.debug("Affichage de la page CGU");
        return "cgu";
    }
}
