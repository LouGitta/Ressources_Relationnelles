package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.ProgressionService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class FavoriteFrontController {

    @Autowired private ProgressionService progressionService;
    @Autowired private UserService userService;
    @Autowired private RessourceService ressourceService;

    @PostMapping("/app/ressource/{id}/favorite")
    public String toggleFavorite(@PathVariable("id") Integer ressourceId, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/app/auth"; 
        }
        Optional<User> userOpt = userService.getByUsername(principal.getName());
        /* MODE DEV : On force l'utilisateur avec l'ID 1
        User user = userService.getById(1).orElseThrow();
        */
        Ressource ressource = ressourceService.getById(ressourceId).orElseThrow();

        Optional<Progression> progOpt = progressionService.getByUserAndRessource(userOpt.get().getId(), ressourceId);
        
        if (progOpt.isPresent()) {
            Progression p = progOpt.get();
            boolean currentStatus = p.isFavorite();
            p.setFavorite(!currentStatus); 
            progressionService.save(p);
        } else {
            Progression p = new Progression();
            p.setUser(userOpt.get());
            p.setRessource(ressource);
            p.setFavorite(true); 
            progressionService.save(p);
        }

        return "redirect:/app/ressource/" + ressourceId;
    }
}