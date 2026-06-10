package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Contrôleur pour gérer la participation aux activités/jeux.
 * Permet à un utilisateur de rejoindre et quitter une activité.
 */
@Controller
public class ParticipationFrontController {

    private static final Logger logger = LoggerFactory.getLogger(ParticipationFrontController.class);

    @Autowired
    private ActivityParticipantRepository participantRepository;

    @Autowired
    private RessourceService ressourceService;

    /**
     * Permet à l'utilisateur de rejoindre une activité.
     *
     * @param id    ID de la ressource (activité)
     * @param model Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.RESSOURCE_DETAIL + "/join")
    public String joinActivity(@PathVariable Integer id, Model model) {
        logger.debug("Tentative de rejoindre l'activité {}", id);

        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        ValidationHelper.validatePositiveId(id, "id");

        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        if (!ressourceOpt.isPresent()) {
            logger.warn("Ressource {} non trouvée", id);
            throw ResourceNotFoundException.notFound("Ressource", id);
        }

        Ressource ressource = ressourceOpt.get();

        // Vérifier que l'utilisateur n'a pas déjà rejoint
        if (participantRepository.findByRessource_IdAndUser_Id(ressource.getId(), currentUser.getId()).isEmpty()) {
            ActivityParticipant participant = new ActivityParticipant();
            participant.setRessource(ressource);
            participant.setUser(currentUser);
            participant.setJoinedAt(LocalDateTime.now());
            participantRepository.save(participant);

            logger.info("L'utilisateur {} a rejoint l'activité {}", currentUser.getId(), id);
        } else {
            logger.debug("L'utilisateur {} participe déjà à l'activité {}", currentUser.getId(), id);
        }

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", id.toString());
    }

    /**
     * Permet à l'utilisateur de quitter une activité.
     *
     * @param id    ID de la ressource (activité)
     * @param model Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.RESSOURCE_DETAIL + "/leave")
    public String leaveActivity(@PathVariable Integer id, Model model) {
        logger.debug("Tentative de quitter l'activité {}", id);

        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        ValidationHelper.validatePositiveId(id, "id");

        Optional<ActivityParticipant> participation = participantRepository.findByRessource_IdAndUser_Id(id, currentUser.getId());
        if (participation.isPresent()) {
            participantRepository.delete(participation.get());
            logger.info("L'utilisateur {} a quitté l'activité {}", currentUser.getId(), id);
        } else {
            logger.debug("L'utilisateur {} ne participe pas à l'activité {}", currentUser.getId(), id);
        }

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", id.toString());
    }
}