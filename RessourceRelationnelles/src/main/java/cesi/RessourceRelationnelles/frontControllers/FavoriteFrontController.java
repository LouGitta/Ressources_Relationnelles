package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.ProgressionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FavoriteFrontController {

    @Autowired private ProgressionService progressionService;

    @PostMapping("/app/ressources/{id}/favorite")
    public String toggleFavorite(@PathVariable("id") Integer ressourceId, HttpServletRequest request, Model model) {
        // GlobalControllerAdvice gère automatiquement : currentUser
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return "redirect:/app/home";
        }
        progressionService.toggleFavorite(user.getId(), ressourceId);
        
        return "redirect:/app/ressources/" + ressourceId;
    }
}