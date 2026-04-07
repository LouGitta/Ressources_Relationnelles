package cesi.RessourceRelationnelles.frontControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.models.Ressource;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RessourceService ressourceService;

    @GetMapping("/app/home")
    public String afficherHome(Model model, HttpServletRequest request) {
        // GlobalControllerAdvice gère automatiquement : isConnected, isMobile, currentUser, isAdmin, isModo
        
        List<Ressource> recentRessources = ressourceService.getRecentRessources();
        model.addAttribute("recentRessources", recentRessources);
        return "home";
    }
}
