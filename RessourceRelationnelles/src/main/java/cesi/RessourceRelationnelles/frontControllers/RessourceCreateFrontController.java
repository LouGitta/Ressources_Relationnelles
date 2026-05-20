package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.security.CurrentUserService;
import cesi.RessourceRelationnelles.models.*; //TODO imports globaux mauvaises pratiques, préférer les imports individuels
import cesi.RessourceRelationnelles.services.*; //TODO imports globaux mauvaises pratiques, préférer les imports individuels
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private CurrentUserService currentUserService;

    @GetMapping("/app/ressources/create")
    public String showCreateForm(Model model, HttpServletRequest request) { //TODO paramètre inutilisé ?

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
            HttpServletRequest request, //TODO variable inutilisé ??
            Authentication authentication) {

        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
        }
        
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