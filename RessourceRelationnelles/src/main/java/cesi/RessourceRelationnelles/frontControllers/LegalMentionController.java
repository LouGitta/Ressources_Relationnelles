package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour afficher les mentions légales.
 */
@Controller
public class LegalMentionController {

    private static final Logger logger = LoggerFactory.getLogger(LegalMentionController.class);

    /**
     * Affiche la page des mentions légales.
     *
     * @return La vue "legalMention"
     */
    @GetMapping(Routes.LEGAL_MENTION)
    public String afficherMentionsLegales() {
        logger.debug("Affichage de la page des mentions légales");
        return "legalMention";
    }
}
