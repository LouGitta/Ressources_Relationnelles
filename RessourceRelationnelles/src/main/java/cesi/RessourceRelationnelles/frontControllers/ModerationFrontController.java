package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.security.CurrentUserService;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ModerationFrontController {

    @Autowired private RessourceService ressourceService;
    @Autowired private CurrentUserService currentUserService;

    @GetMapping("/app/ressources/moderation")
    public String afficherModeration(Model model, HttpServletRequest request, Authentication authentication) { //TODO paramètre inutilisé ?
        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
        }
        if (currentUser.getRole() == Role.CITIZEN) {
            return "redirect:/home"; 
        }

        List<Ressource> pendingRessources = ressourceService.getPendingRessources();
        model.addAttribute("pendingRessources", pendingRessources);

        return "moderation";
    }

    @PostMapping("/app/ressources/moderation/{id}/accept")
    public String acceptRessource(@PathVariable Integer id, HttpServletRequest request, Authentication authentication) { //TODO paramètre inutilisé ?

        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
        }
        if (currentUser.getRole() == Role.MODERATOR) {
            ressourceService.updateStatus(id, RessourceStatus.published);
        }
        return "redirect:/app/ressources/moderation";
    }

    // --- REFUSER UNE RESSOURCE ---
    @PostMapping("/app/ressources/moderation/{id}/reject")
    public String rejectRessource(@PathVariable Integer id, HttpServletRequest request, Authentication authentication) { //TODO paramètre inutilisé ?

        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
        }
        if (currentUser.getRole() == Role.MODERATOR) {
            ressourceService.updateStatus(id, RessourceStatus.rejected);
        }
        return "redirect:/app/ressources/moderation";
    }
}