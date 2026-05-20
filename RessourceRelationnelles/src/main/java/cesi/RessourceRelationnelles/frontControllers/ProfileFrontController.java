package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.security.CurrentUserService;
import cesi.RessourceRelationnelles.models.*; //TODO imports globaux mauvaises pratiques, préférer les imports individuels
import cesi.RessourceRelationnelles.repositories.UserRepository;
import cesi.RessourceRelationnelles.services.*; //TODO imports globaux mauvaises pratiques, préférer les imports individuels
import jakarta.servlet.http.HttpServletRequest; //TODO import inutilisé ?
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/app/profile")
    public String afficherProfil(Model model, Authentication authentication) {

        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
        }
        // Ajouter aussi 'user' pour les templates
        model.addAttribute("user", currentUser);

        // 1. Ses ressources créées
        List<Ressource> myRessources = ressourceService.getByUser(currentUser.getId());
        model.addAttribute("myRessources", myRessources);

        // 2. Ses favoris (via Progression)
        List<Progression> myFavorites = progressionService.getFavoritesByUser(currentUser.getId());
        model.addAttribute("myFavorites", myFavorites);

        // 3. Sa liste d'amis (méthode déjà existante dans ton FriendService)
        List<Friend> myFriends = friendService.getAllAcceptedFriends(currentUser.getId());
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