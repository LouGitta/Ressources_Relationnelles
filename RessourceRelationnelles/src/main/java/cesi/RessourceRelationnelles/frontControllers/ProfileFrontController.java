package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;
import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import cesi.RessourceRelationnelles.services.FriendService;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour afficher le profil utilisateur.
 * Affiche les ressources, favoris, amis, etc.
 */
@Controller
public class ProfileFrontController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileFrontController.class);

    @Autowired
    private RessourceService ressourceService;
    
    @Autowired
    private ProgressionService progressionService;
    
    @Autowired
    private FriendService friendService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Affiche le profil de l'utilisateur actuel.
     * GlobalControllerAdvice gère automatiquement: isConnected, currentUser
     *
     * @param model   Le modèle pour la vue
     * @param request La requête HTTP
     * @return La vue "profile"
     */
    @GetMapping(Routes.PROFILE)
    public String afficherProfil(Model model, HttpServletRequest request) {
        logger.debug("Affichage du profil utilisateur");
        
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        logger.debug("Chargement des données du profil pour l'utilisateur {}", user.getId());
        
        // Ajouter aussi 'user' pour les templates Thymeleaf
        model.addAttribute("user", user);

        // 1. Ses ressources créées
        List<Ressource> myRessources = ressourceService.getByUser(user.getId());
        model.addAttribute("myRessources", myRessources);
        logger.debug("Ressources de l'utilisateur: {}", myRessources.size());

        // 2. Ses favoris (via Progression)
        List<Progression> myFavorites = progressionService.getFavoritesByUser(user.getId());
        model.addAttribute("myFavorites", myFavorites);
        logger.debug("Favoris de l'utilisateur: {}", myFavorites.size());

        // 3. Sa liste d'amis
        List<Friend> myFriends = friendService.getAllAcceptedFriends(user.getId());
        model.addAttribute("myFriends", myFriends);
        logger.debug("Amis acceptés de l'utilisateur: {}", myFriends.size());

        // 4. Ses demandes d'amis reçues (filtrées en attente)
        List<Friend> incomingRequests = friendService.getByUser2(user.getId())
                .stream()
                .filter(f -> f.getStatus() == FriendStatus.pending)
                .collect(Collectors.toList());
        model.addAttribute("incomingRequests", incomingRequests);
        logger.debug("Demandes d'ami en attente: {}", incomingRequests.size());

        return "profile";
    }
}
