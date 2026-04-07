package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Activity;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ActivityFrontController {

    @Autowired
    private ActivityService activityService;

    // --- 1. AFFICHER LA LISTE DES ACTIVITÉS ---
    @GetMapping("/app/activities")
    public String listActivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);

        // Pour savoir quels boutons afficher (Rejoindre ou Quitter), on liste les IDs des activités déjà rejointes
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser != null) {
            List<Integer> joinedActivityIds = activityService.getUserParticipations(currentUser.getId())
                    .stream()
                    .map(p -> p.getActivity().getId())
                    .collect(Collectors.toList());
            model.addAttribute("joinedActivityIds", joinedActivityIds);
        }

        return "activities";
    }

    // --- 2. REJOINDRE UNE ACTIVITÉ ---
    @PostMapping("/app/activities/{id}/join")
    public String joinActivity(@PathVariable Integer id, Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        activityService.getActivityById(id).ifPresent(activity -> {
            activityService.joinActivity(activity, currentUser);
        });

        return "redirect:/app/activities";
    }

    // --- 3. QUITTER UNE ACTIVITÉ ---
    @PostMapping("/app/activities/{id}/leave")
    public String leaveActivity(@PathVariable Integer id, Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        activityService.leaveActivity(id, currentUser.getId());

        return "redirect:/app/activities";
    }
}