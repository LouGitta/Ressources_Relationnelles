package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour afficher la page de création de compte et gérer l'inscription.
 */
@Controller
public class CreateAccountController {

    private static final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);

    @Autowired
    private UserService userService;

    /**
     * Affiche le formulaire de création de compte.
     *
     * @return La vue "createAccount"
     */
    @GetMapping(Routes.CREATE_ACCOUNT)
    public String afficherCreateAccount() {
        logger.debug("Affichage du formulaire de création de compte");
        return "createAccount";
    }

    /**
     * Gère la soumission du formulaire de création de compte.
     *
     * @param username        Nom d'utilisateur choisi
     * @param email           Adresse email de l'utilisateur
     * @param password        Mot de passe choisi
     * @param confirmPassword Confirmation du mot de passe
     * @param model           Modèle pour passer les données ou erreurs à la vue
     * @return Redirection vers la connexion ou retour à la vue avec erreur
     */
    @PostMapping(Routes.CREATE_ACCOUNT)
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        logger.info("Tentative de création de compte pour l'utilisateur: {}", username);

        try {
            // Validation des champs obligatoires
            ValidationHelper.validateNotBlank(username, "Nom d'utilisateur");
            ValidationHelper.validateNotBlank(email, "Email");
            ValidationHelper.validateNotBlank(password, "Mot de passe");
            ValidationHelper.validateNotBlank(confirmPassword, "Confirmation du mot de passe");

            // Nettoyage et validation de format
            username = username.trim();
            email = email.trim();

            ValidationHelper.validateLength(username, AppConstants.USERNAME_MIN_LENGTH, AppConstants.USERNAME_MAX_LENGTH, "Nom d'utilisateur");
            ValidationHelper.validateEmail(email, "Email");
            ValidationHelper.validateLength(password, AppConstants.PASSWORD_MIN_LENGTH, 255, "Mot de passe");

            // Vérification de la correspondance des mots de passe
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }

            // Vérification de l'unicité du nom d'utilisateur et de l'email
            if (userService.getByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Le nom d'utilisateur est déjà utilisé");
            }

            if (userService.getByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Cette adresse email est déjà enregistrée");
            }

            // Création de l'utilisateur
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // Stockage tel quel (sans encodage, conformément au reste du projet)
            user.setRole(Role.CITIZEN);
            user.setActive(true);

            userService.save(user);
            logger.info("Compte créé avec succès pour l'utilisateur: {}", username);

            return Routes.REDIRECT_LOGIN;

        } catch (IllegalArgumentException e) {
            logger.warn("Erreur lors de la création de compte pour {}: {}", username, e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "createAccount";
        }
    }
}