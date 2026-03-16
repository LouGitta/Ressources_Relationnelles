package cesi.RessourceRelationnelles.frontControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cesi.RessourceRelationnelles.utils.DeviceDetector;

@Controller
public class HomeController {

    @Autowired
    private DeviceDetector deviceDetector;

    @GetMapping("/home")
    public String afficherHome(Model model, HttpServletRequest request) {
        // Détecte si l'utilisateur est connecté
        // Vérifie si l'utilisateur est authentifié via la session
        boolean isConnected = request.getUserPrincipal() != null;
        model.addAttribute("isConnected", isConnected);
        
        // Détecte si c'est un appareil mobile
        model.addAttribute("isMobile", deviceDetector.isMobile(request));
        
        return "home";
    }
}
