package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.exceptions.ValidationException;
import cesi.RessourceRelationnelles.models.Category;
import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RelationService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.TypeService;
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
 * Contrôleur pour créer une nouvelle ressource.
 * Gère l'affichage du formulaire et la sauvegarde de la ressource.
 * La logique métier d'initialisation (statut, date, vues) est déléguée à {@link RessourceService#create}.
 */
@Controller
public class RessourceCreateFrontController {

    private static final Logger logger = LoggerFactory.getLogger(RessourceCreateFrontController.class);

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private RelationService relationService;

    /**
     * Affiche le formulaire de création de ressource.
     *
     * @param model Le modèle pour la vue
     * @return La vue "ressourceCreate"
     */
    @GetMapping(Routes.RESSOURCE_CREATE)
    public String showCreateForm(Model model) {
        logger.debug("Affichage du formulaire de création de ressource");

        User currentUser = (User) model.asMap().get("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("types", typeService.getAll());
        model.addAttribute("relations", relationService.getAll());
        model.addAttribute("visibilities", Visibility.values());

        logger.info("Formulaire de création de ressource prêt pour l'utilisateur {}", currentUser.getId());
        return "ressourceCreate";
    }

    /**
     * Sauvegarde une nouvelle ressource.
     * La validation des longueurs est faite ici ; la logique métier d'initialisation
     * (statut {@code pending}, date de création, vues à 0) est délégué au service.
     *
     * @param title      Titre de la ressource (requis)
     * @param content    Contenu de la ressource (requis)
     * @param categoryId ID de la catégorie (requis)
     * @param typeId     ID du type (requis)
     * @param relationId ID de la relation (requis)
     * @param visibility Visibilité de la ressource
     * @param model      Le modèle pour la vue
     * @return Redirection vers le catalogue
     */
    @PostMapping(Routes.RESSOURCE_CREATE)
    public String saveRessource(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer categoryId,
            @RequestParam Integer typeId,
            @RequestParam Integer relationId,
            @RequestParam(defaultValue = "PUBLIC") Visibility visibility,
            Model model) {

        logger.debug("Création d'une nouvelle ressource - titre: {}", title);

        User currentUser = (User) model.asMap().get("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié lors de la création");
            throw UnauthorizedException.notAuthenticated();
        }

        // Validation des paramètres obligatoires
        ValidationHelper.validateNotBlank(title, "title");
        ValidationHelper.validateNotBlank(content, "content");
        ValidationHelper.validatePositiveId(categoryId, "categoryId");
        ValidationHelper.validatePositiveId(typeId, "typeId");
        ValidationHelper.validatePositiveId(relationId, "relationId");
        ValidationHelper.validateLength(title.trim(), AppConstants.TITLE_MIN_LENGTH, AppConstants.TITLE_MAX_LENGTH, "title");
        ValidationHelper.validateLength(content.trim(), AppConstants.CONTENT_MIN_LENGTH, AppConstants.CONTENT_MAX_LENGTH, "content");

        // Résolution des associations
        Category category = categoryService.getById(categoryId).orElse(null);
        Type type = typeService.getById(typeId).orElse(null);
        Relation relation = relationService.getById(relationId).orElse(null);

        // Délégation complète de la création métier au service
        var saved = ressourceService.create(title, content, visibility, currentUser, category, type, relation);

        logger.info("Ressource créée avec succès par l'utilisateur {}. ID: {}", currentUser.getId(), saved.getId());
        return Routes.REDIRECT_RESSOURCES;
    }
}