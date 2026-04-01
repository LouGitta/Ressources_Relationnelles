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

    @GetMapping("/app/home")
    public String afficherHome(Model model, HttpServletRequest request) {
        // Détecte si l'utilisateur est connecté
        // --- MODE DEV : On simule la connexion ---
        boolean isConnected = true;
        model.addAttribute("isConnected", isConnected);
        
        /* PROD - Vérification réelle de sécurité (quand Spring Security sera implémenté)
        boolean isConnected = request.getUserPrincipal() != null;
        model.addAttribute("isConnected", isConnected);
        */
        
        // Détecte si c'est un appareil mobile
        model.addAttribute("isMobile", deviceDetector.isMobile(request));
        
        // --- MODE DEV : On force l'utilisateur ID 1 ---
        cesi.RessourceRelationnelles.models.User currentUser = userService.getById(1).orElseThrow();
        boolean isAdmin = currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.moderator || 
                        currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.administrator || 
                      currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.super_admin;
        model.addAttribute("isAdmin", isAdmin);
        
        /* PROD - Vérification réelle de sécurité (quand Spring Security sera implémenté)
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            cesi.RessourceRelationnelles.models.User currentUser = userService.getByUsername(principal.getName()).orElseThrow();
            boolean isAdmin = currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.moderator || 
                            currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.administrator || 
                          currentUser.getRole() == cesi.RessourceRelationnelles.models.Role.super_admin;
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("isAdmin", false);
        }
        */
        List<Ressource> recentRessources = ressourceService.getRecentRessources();
        model.addAttribute("recentRessources", recentRessources);
        return "home";
    }
}
