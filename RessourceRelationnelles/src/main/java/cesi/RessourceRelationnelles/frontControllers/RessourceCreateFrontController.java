package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.exceptions.ValidationException;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RelationService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.TypeService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Contrôleur pour créer une nouvelle ressource.
 * Gère l'affichage du formulaire et la sauvegarde de la ressource.
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
     * @param model   Le modèle pour la vue
     * @param request La requête HTTP
     * @return La vue "ressourceCreate"
     */
    @GetMapping(Routes.RESSOURCE_CREATE)
    public String showCreateForm(Model model, HttpServletRequest request) {
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
     * Validation des paramètres, création et sauvegarde.
     *
     * @param title      Titre de la ressource (requis)
     * @param content    Contenu de la ressource (requis)
     * @param categoryId ID de la catégorie (requis)
     * @param typeId     ID du type (requis)
     * @param relationId ID de la relation (requis)
     * @param visibility Visibilité de la ressource
     * @param request    La requête HTTP
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
            HttpServletRequest request,
            Model model) {

        logger.debug("Création d'une nouvelle ressource - titre: {}", title);

        User currentUser = (User) model.asMap().get("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié lors de la création");
            throw UnauthorizedException.notAuthenticated();
        }

        // Validation des paramètres
        ValidationHelper.validateNotBlank(title, "title");
        ValidationHelper.validateNotBlank(content, "content");
        ValidationHelper.validatePositiveId(categoryId, "categoryId");
        ValidationHelper.validatePositiveId(typeId, "typeId");
        ValidationHelper.validatePositiveId(relationId, "relationId");

        title = title.trim();
        content = content.trim();

        if (title.length() < AppConstants.TITLE_MIN_LENGTH) {
            logger.warn("Titre trop court: {} caractères", title.length());
            throw ValidationException.invalidField("title", 
                "Le titre doit contenir au moins " + AppConstants.TITLE_MIN_LENGTH + " caractères");
        }

        if (content.length() < AppConstants.CONTENT_MIN_LENGTH) {
            logger.warn("Contenu trop court: {} caractères", content.length());
            throw ValidationException.invalidField("content",
                "Le contenu doit contenir au moins " + AppConstants.CONTENT_MIN_LENGTH + " caractères");
        }

        // Créer la ressource
        Ressource ressource = new Ressource();
        ressource.setTitle(title);
        ressource.setContent(content);
        ressource.setVisibility(visibility);
        ressource.setCreatedAt(LocalDateTime.now());
        ressource.setStatus(RessourceStatus.pending);
        ressource.setViews(0);
        ressource.setUser(currentUser);

        categoryService.getById(categoryId).ifPresent(ressource::setCategory);
        typeService.getById(typeId).ifPresent(ressource::setType);
        relationService.getById(relationId).ifPresent(ressource::setRelation);

        // Sauvegarder
        ressourceService.save(ressource);

        logger.info("Ressource créée avec succès par l'utilisateur {}. ID: {}", currentUser.getId(), ressource.getId());
        return Routes.REDIRECT_RESSOURCES;
    }
}