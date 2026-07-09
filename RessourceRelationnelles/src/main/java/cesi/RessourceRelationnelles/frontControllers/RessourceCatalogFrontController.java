package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Contrôleur pour afficher le catalogue des ressources publiées.
 * Gère la recherche et le filtrage par titre, catégorie et visibilité.
 */
@Controller
public class RessourceCatalogFrontController {

    private static final Logger logger = LoggerFactory.getLogger(RessourceCatalogFrontController.class);

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Affiche le catalogue des ressources avec filtrage.
     *
     * @param title      Titre à rechercher (optionnel)
     * @param categoryId ID de la catégorie à filtrer (optionnel)
     * @param visibility Visibilité à filtrer (optionnel)
     * @param model      Le modèle pour la vue
     * @param request    La requête HTTP
     * @return La vue "ressources"
     */
    @GetMapping(Routes.RESSOURCES)
    public String afficherCatalogue(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Visibility visibility,
            Model model, 
            HttpServletRequest request) {
        
        logger.debug("Affichage du catalogue. Filtres: title={}, categoryId={}, visibility={}", 
            title, categoryId, visibility);

        // Validation des paramètres
        if (title != null && !title.trim().isEmpty()) {
            title = ValidationHelper.sanitizeInput(title);
            logger.debug("Titre après sanitization: {}", title);
        }
        
        if (categoryId != null && categoryId <= 0) {
            logger.warn("Category ID invalide: {}", categoryId);
            categoryId = null;
        }

        // Charger les données de référence
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("selectedTitle", title);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedVisibility", visibility);

        // Rechercher et filtrer les ressources publiées
        List<Ressource> ressources = ressourceService.searchAndFilter(
            title, categoryId, visibility, RessourceStatus.published);
        
        logger.info("Catalogue chargé: {} ressources trouvées", ressources.size());
        model.addAttribute("ressources", ressources);

        return "ressources";
    }
}