package cesi.RessourceRelationnelles.frontControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cesi.RessourceRelationnelles.utils.DeviceDetector;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.services.UserService;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DeviceDetector deviceDetector;
    
    @Autowired
    private RessourceService ressourceService;


    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String afficherHome(Model model, HttpServletRequest request) {
        // Détecte si l'utilisateur est connecté
        // Vérifie si l'utilisateur est authentifié via la session
        boolean isConnected = true;
        // debug boolean isConnected = request.getUserPrincipal() != null;
        model.addAttribute("isConnected", isConnected);
        
        // Détecte si c'est un appareil mobile
        model.addAttribute("isMobile", deviceDetector.isMobile(request));
        
        cesi.RessourceRelationnelles.models.User currentUser = userService.getById(1).orElseThrow();
        boolean isAdmin = currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.moderator || 
                        currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.administrator || 
                      currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.super_admin;
        model.addAttribute("isAdmin", isAdmin);
        List<Ressource> recentRessources = ressourceService.getRecentRessources();
        model.addAttribute("recentRessources", recentRessources);
        return "home";
    }
}
