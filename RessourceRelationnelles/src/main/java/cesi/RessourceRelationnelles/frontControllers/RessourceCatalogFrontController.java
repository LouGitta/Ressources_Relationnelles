package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RessourceCatalogFrontController {

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/app/ressources")
    public String afficherCatalogue(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Visibility visibility,
            Model model, HttpServletRequest request) {

        // GlobalControllerAdvice gère automatiquement : isConnected, isMobile
        
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("selectedTitle", title);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedVisibility", visibility);

        List<Ressource> ressources = ressourceService.searchAndFilter(title, categoryId, visibility, RessourceStatus.published);
        model.addAttribute("ressources", ressources);

        return "ressources";
    }
}