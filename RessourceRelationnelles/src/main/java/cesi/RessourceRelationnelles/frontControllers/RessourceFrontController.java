package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.security.CurrentUserService;
import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private ActivityParticipantRepository activityParticipantRepository;

    @Autowired
    private ProgressionService progressionService;

    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/app/ressources/{id}")
    public String afficherRessource(@PathVariable Integer id, Model model, Authentication authentication, HttpServletRequest request) {
        User currentUser = currentUserService.get(authentication).orElse(null);
        if (currentUser == null) {
            return "redirect:/app/login";
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

            // --- GESTION SPÉCIFIQUE DES ACTIVITÉS / JEUX ---
            boolean isParticipating = false;
            List<ActivityParticipant> participants = new ArrayList<>();
            
            // On simule que la date est valide comme convenu
            boolean isDateValid = true; 
            model.addAttribute("isDateValid", isDateValid);

            // On vérifie si la ressource a bien un type et si c'est une activité
            if (ressource.getType() != null && "Activité / Jeu à réaliser".equals(ressource.getType().getName())) {
                
                // 1. Récupérer tous les participants de cette ressource
                participants = activityParticipantRepository.findByRessource_Id(ressource.getId());
                
                // 2. Vérifier si l'utilisateur connecté en fait partie
                isParticipating = activityParticipantRepository.findByRessource_IdAndUser_Id(ressource.getId(), currentUser.getId()).isPresent();
            }

            // 3. Envoyer les données à la vue (ressource.html)
            model.addAttribute("isParticipating", isParticipating);
            model.addAttribute("participants", participants);

            return "ressource";
        }
        
        return "redirect:/app/home";
    }
}