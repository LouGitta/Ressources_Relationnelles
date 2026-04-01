package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.*;
import cesi.RessourceRelationnelles.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileFrontController {

    @Autowired
    private UserService userService;
    @Autowired
    private RessourceService ressourceService;
    @Autowired
    private ProgressionService progressionService;
    @Autowired
    private FriendService friendService;

    @GetMapping("/app/profile")
    public String afficherProfil(Model model, HttpServletRequest request) {
        // --- MODE DEV : On force l'utilisateur ID 1 ---
        User user = userService.getById(1).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("isConnected", true);
        
        /* PROD - Vérification réelle de sécurité (quand Spring Security sera implémenté)
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/app/auth";
        }
        User user = userService.getByUsername(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("isConnected", true);
        */

        // 1. Ses ressources créées
        List<Ressource> myRessources = ressourceService.getByUser(user.getId());
        model.addAttribute("myRessources", myRessources);

        // 2. Ses favoris (via Progression)
        List<Progression> myFavorites = progressionService.getFavoritesByUser(user.getId());
        model.addAttribute("myFavorites", myFavorites);

        // 3. Sa liste d'amis (méthode déjà existante dans ton FriendService)
        List<Friend> myFriends = friendService.getAllAcceptedFriends(user.getId());
        model.addAttribute("myFriends", myFriends);

        return "profile";
    }
}