package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceDetector deviceDetector;

    @Value("${dev.mode:false}")
    private boolean devMode;

    @Value("${dev.mode.default-user-id:3}")
    private Integer defaultUserId;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        
        model.addAttribute("isMobile", deviceDetector.isMobile(request));

        Principal principal = request.getUserPrincipal();
        
        // En mode DEV, simuler une connexion automatique
        boolean isConnected = (principal != null) || devMode;
        model.addAttribute("isConnected", isConnected);

        boolean isAdmin = false;
        boolean isModo  = false;
        
        if (isConnected) {
            User currentUser = null;
            
            // En mode DEV, utiliser l'utilisateur par défaut
            if (devMode && principal == null) {
                Optional<User> userOpt = userService.getById(defaultUserId);
                if (userOpt.isPresent()) {
                    currentUser = userOpt.get();
                }
            } else if (principal != null) {
                // En mode PROD, le principal Spring Security contient l'email (UserDetailsService configure par email)
                String login = principal.getName();
                // Tentative unique par email (cas standard), puis fallback username
                Optional<User> userOpt = userService.getByEmail(login);
                if (userOpt.isEmpty()) {
                    userOpt = userService.getByUsername(login);
                }
                if (userOpt.isPresent()) {
                    currentUser = userOpt.get();
                }
            }
            
            if (currentUser != null) {
                // On rend l'utilisateur courant disponible sur TOUTES les pages
                model.addAttribute("currentUser", currentUser); 
                
                Role role = currentUser.getRole();
                if (role == Role.ADMINISTRATOR || role == Role.SUPERADMIN) {
                    isAdmin = true;
                } else if (role == Role.MODERATOR) {
                    isModo  = true;
                }
            }
        }
        
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isModo", isModo);
    }
}