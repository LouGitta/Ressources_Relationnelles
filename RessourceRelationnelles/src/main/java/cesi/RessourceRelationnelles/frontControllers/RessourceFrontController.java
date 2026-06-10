package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour afficher les détails d'une ressource.
 * Gère l'affichage des commentaires, des participants aux activités, et du statut de favori.
 */
@Controller
public class RessourceFrontController {

    private static final Logger logger = LoggerFactory.getLogger(RessourceFrontController.class);

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private cesi.RessourceRelationnelles.services.PermissionService permissionService;

    @Autowired
    private ProgressionService progressionService;

    /**
     * Affiche les détails d'une ressource avec ses commentaires et participants.
     *
     * @param id      ID de la ressource
     * @param model   Le modèle pour la vue
     * @param request La requête HTTP
     * @return La vue "ressource"
     */
    @GetMapping(Routes.RESSOURCE_DETAIL)
    public String afficherRessource(@PathVariable Integer id, Model model, HttpServletRequest request) {
        logger.debug("Affichage de la ressource {}", id);
        
        User currentUser = (User) model.asMap().get("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        if (!ressourceOpt.isPresent()) {
            logger.warn("Ressource {} non trouvée", id);
            throw ResourceNotFoundException.notFound("Ressource", id);
        }

        Ressource ressource = ressourceOpt.get();
        logger.debug("Ressource {} trouvée, status: {}", id, ressource.getStatus());

        // Vérifier les permissions via PermissionService
        if (!permissionService.canViewRessource(currentUser.getId(), id)) {
            logger.warn("Accès refusé à la ressource {}. User: {}, Ressource status: {}", 
                id, currentUser.getId(), ressource.getStatus());
            throw ResourceNotFoundException.notFound("Ressource", id);
        }

        model.addAttribute("ressource", ressource);
        logger.info("Ressource {} affichée pour l'utilisateur {}", id, currentUser.getId());

        // Ajouter les commentaires
        model.addAttribute("comments", commentService.getByRessource(id));
        logger.debug("Commentaires de la ressource {} chargés", id);

        // Ajouter le rôle de l'utilisateur
        model.addAttribute("userRole", currentUser.getRole().name());

        // Vérifier si la ressource est en favori
        boolean isFavorite = false;
        Optional<Progression> progOpt = progressionService.getByUserAndRessource(currentUser.getId(), id);
        if (progOpt.isPresent() && progOpt.get().isFavorite()) {
            isFavorite = true;
            logger.debug("Ressource {} est en favori pour l'utilisateur {}", id, currentUser.getId());
        }
        model.addAttribute("isFavorite", isFavorite);

        // --- GESTION SPÉCIFIQUE DES ACTIVITÉS / JEUX ---
        boolean isParticipating = false;
        List<ActivityParticipant> participants = new ArrayList<>();

        // On vérifie si la ressource a bien un type et si c'est une activité
        if (ressource.getType() != null && "Activité / Jeu à réaliser".equals(ressource.getType().getName())) {
            logger.debug("Ressource {} est une activité/jeu", id);
            
            // 1. Récupérer tous les participants de cette ressource via le Service
            participants = ressourceService.getParticipants(ressource.getId());
            logger.debug("Nombre de participants: {}", participants.size());
            
            // 2. Vérifier si l'utilisateur connecté en fait partie
            isParticipating = ressourceService.isUserParticipating(ressource.getId(), currentUser.getId());
            logger.debug("Utilisateur {} participe: {}", currentUser.getId(), isParticipating);
        }

        // 3. Envoyer les données à la vue (ressource.html)
        model.addAttribute("isParticipating", isParticipating);
        model.addAttribute("participants", participants);

        // On simule que la date est valide comme convenu
        model.addAttribute("isDateValid", true);

        return "ressource";
    }
}