package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
public class RessourceFrontController {

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProgressionService progressionService;

    @Autowired
    private DeviceDetector deviceDetector;

    @GetMapping("/app/ressource/{id}")
    public String afficherRessource(@PathVariable Integer id, Model model, HttpServletRequest request) {

        

        // --- MODE DEV : On force la connexion pour l'affichage ---
        /*boolean isConnected = true;
        model.addAttribute("isConnected", isConnected);
        model.addAttribute("isMobile", deviceDetector.isMobile(request));

        // --- MODE DEV : On force l'utilisateur ID 1 ---
        User currentUser = userService.getById(1).orElseThrow();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userRole", currentUser.getRole().name());
        */
        Principal principal = request.getUserPrincipal();
        if (principal == null) return "redirect:/app/auth";
        User currentUser = userService.getByUsername(principal.getName()).orElseThrow();
        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        
        if (ressourceOpt.isPresent()) {
            Ressource ressource = ressourceOpt.get();

            boolean isPublished = ressource.getStatus() == cesi.RessourceRelationnelles.models.RessourceStatus.published;
            boolean isAuthor = ressource.getUser().getId().equals(currentUser.getId());

            if (!isPublished && !isAuthor) {
                return "redirect:/app/ressource"; 
            }

            model.addAttribute("ressource", ressource);
            model.addAttribute("comments", commentService.getByRessource(id));

            boolean isFavorite = false;
            Optional<Progression> progOpt = progressionService.getByUserAndRessource(currentUser.getId(), id);

            if (progOpt.isPresent() && progOpt.get().isFavorite()) {
                isFavorite = true;
            }
            model.addAttribute("isFavorite", isFavorite);

            return "ressource";
        }
        
        return "redirect:/home";
    }
}