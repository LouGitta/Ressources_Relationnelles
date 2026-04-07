package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RessourceFrontController {

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProgressionService progressionService;

    @GetMapping("/app/ressources/{id}")
    public String afficherRessource(@PathVariable Integer id, Model model, HttpServletRequest request) {
        // GlobalControllerAdvice gère automatiquement : isConnected, isMobile, currentUser
        User currentUser = (User) model.asMap().get("currentUser");
        if (currentUser == null) {
            return "redirect:/app/home";
        }
        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        
        if (ressourceOpt.isPresent()) {
            Ressource ressource = ressourceOpt.get();

            boolean isPublished = ressource.getStatus() == cesi.RessourceRelationnelles.models.RessourceStatus.published;
            boolean isAuthor = ressource.getUser().getId().equals(currentUser.getId());

            if (!isPublished && !isAuthor) {
                return "redirect:/app/ressources"; 
            }

            model.addAttribute("ressource", ressource);
            model.addAttribute("comments", commentService.getByRessource(id));
            model.addAttribute("userRole", currentUser.getRole().name());

            boolean isFavorite = false;
            Optional<Progression> progOpt = progressionService.getByUserAndRessource(currentUser.getId(), id);

            if (progOpt.isPresent() && progOpt.get().isFavorite()) {
                isFavorite = true;
            }
            model.addAttribute("isFavorite", isFavorite);

            return "ressource";
        }
        
        return "redirect:/app/home";
    }
}