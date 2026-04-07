package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.services.RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ParticipationFrontController {

    @Autowired private ActivityParticipantRepository participantRepository;
    @Autowired private RessourceService ressourceService;

    // --- REJOINDRE L'ACTIVITÉ ---
    @PostMapping("/app/ressources/{id}/join")
    public String joinActivity(@PathVariable Integer id, Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        if (ressourceOpt.isPresent()) {
            Ressource ressource = ressourceOpt.get();
            
            if (participantRepository.findByRessource_IdAndUser_Id(ressource.getId(), currentUser.getId()).isEmpty()) {
                ActivityParticipant p = new ActivityParticipant();
                p.setRessource(ressource);
                p.setUser(currentUser);
                p.setJoinedAt(LocalDateTime.now());
                participantRepository.save(p);
            }
        }
        return "redirect:/app/ressources/" + id;
    }

    // --- PARTIR DE L'ACTIVITÉ ---
    @PostMapping("/app/ressources/{id}/leave")
    public String leaveActivity(@PathVariable Integer id, Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        Optional<ActivityParticipant> participation = participantRepository.findByRessource_IdAndUser_Id(id, currentUser.getId());
        participation.ifPresent(p -> participantRepository.delete(p));

        return "redirect:/app/ressources/" + id;
    }
}