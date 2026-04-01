package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.*;
import cesi.RessourceRelationnelles.services.*;
import cesi.RessourceRelationnelles.utils.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class RessourceCreateFrontController {

    @Autowired
    private RessourceService ressourceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceDetector deviceDetector;

    @GetMapping("/app/ressources/create")
    public String showCreateForm(Model model, HttpServletRequest request) {
        
        /* MODE DEV : On désactive la redirection de sécurité
        if (request.getUserPrincipal() == null) {
            return "redirect:/app/auth";
        }
        */

        model.addAttribute("isConnected", true);
        model.addAttribute("isMobile", deviceDetector.isMobile(request));
        
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("types", typeService.getAll());
        model.addAttribute("relations", relationService.getAll());
        model.addAttribute("visibilities", Visibility.values());

        return "ressourceCreate";
    }

    @PostMapping("/app/ressources/create")
    public String saveRessource(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer categoryId,
            @RequestParam Integer typeId,
            @RequestParam Integer relationId,
            @RequestParam Visibility visibility,
            HttpServletRequest request) {

        // --- MODE DEV : On force l'utilisation de l'utilisateur avec l'ID 1 ---
        User currentUser = userService.getById(1).orElseThrow();
        
        /* PROD - Vérification réelle de sécurité (quand Spring Security sera implémenté)
        Principal principal = request.getUserPrincipal();
        if (principal == null) return "redirect:/app/auth";
        User currentUser = userService.getByUsername(principal.getName()).orElseThrow();
        */
        
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

        ressourceService.save(ressource);

        return "redirect:/app/ressources"; 
    }
}