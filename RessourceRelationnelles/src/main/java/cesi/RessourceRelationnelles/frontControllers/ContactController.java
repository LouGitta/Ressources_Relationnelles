package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour afficher la page de contact.
 */
@Controller
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    /**
     * Affiche la page de contact.
     *
     * @return La vue "contact"
     */
    @GetMapping(Routes.CONTACT)
    public String afficherContact() {
        logger.debug("Affichage de la page de contact");
        return "contact";
    }
}