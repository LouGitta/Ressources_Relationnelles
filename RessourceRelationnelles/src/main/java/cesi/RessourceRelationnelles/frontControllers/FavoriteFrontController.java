package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class FavoriteFrontController {

    @Autowired private ProgressionService progressionService;
    @Autowired private UserService userService;

    @PostMapping("/app/ressources/{id}/favorite")
    public String toggleFavorite(@PathVariable("id") Integer ressourceId, HttpServletRequest request) {
        // --- MODE DEV : On force l'utilisateur avec l'ID 1 ---
        Optional<User> userOpt = userService.getById(1);
        progressionService.toggleFavorite(userOpt.get().getId(), ressourceId);
        
        /* PROD - Vérification réelle de sécurité (quand Spring Security sera implémenté)
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/app/auth"; 
        }
        Optional<User> userOpt = userService.getByUsername(principal.getName());
        progressionService.toggleFavorite(userOpt.get().getId(), ressourceId);
        */
        
        return "redirect:/app/ressources/" + ressourceId;
    }
}