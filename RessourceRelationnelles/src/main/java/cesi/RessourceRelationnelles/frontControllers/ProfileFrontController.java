package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.*;
import cesi.RessourceRelationnelles.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileFrontController {

    @Autowired
    private RessourceService ressourceService;
    @Autowired
    private ProgressionService progressionService;
    @Autowired
    private FriendService friendService;

    @GetMapping("/app/profile")
    public String afficherProfil(Model model, HttpServletRequest request) {
        // GlobalControllerAdvice gère automatiquement : isConnected, currentUser
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return "redirect:/app/home";
        }

        // Ajouter aussi 'user' pour les templates
        model.addAttribute("user", user);

        // 1. Ses ressources créées
        List<Ressource> myRessources = ressourceService.getByUser(user.getId());
        model.addAttribute("myRessources", myRessources);

        // 2. Ses favoris (via Progression)
        List<Progression> myFavorites = progressionService.getFavoritesByUser(user.getId());
        model.addAttribute("myFavorites", myFavorites);

        // 3. Sa liste d'amis (méthode déjà existante dans ton FriendService)
        List<Friend> myFriends = friendService.getAllAcceptedFriends(user.getId());
        model.addAttribute("myFriends", myFriends);

        // 4. Ses demandes d'amis reçues (FILTRÉES en attente)
        List<Friend> incomingRequests = friendService.getByUser2(user.getId())
                .stream()
                .filter(f -> f.getStatus() == FriendStatus.pending)
                .collect(Collectors.toList());
        model.addAttribute("incomingRequests", incomingRequests);

        return "profile";
    }
}