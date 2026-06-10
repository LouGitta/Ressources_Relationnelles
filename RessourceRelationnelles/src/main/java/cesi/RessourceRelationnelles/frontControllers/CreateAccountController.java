package cesi.RessourceRelationnelles.frontControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour afficher la page de création de compte.
 */
@Controller
public class CreateAccountController {

    private static final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);

    /**
     * Affiche le formulaire de création de compte.
     *
     * @return La vue "createAccount"
     */
    @GetMapping("/app/create-account")
    public String afficherCreateAccount() {
        logger.debug("Affichage du formulaire de création de compte");
        return "createAccount";
    }
}