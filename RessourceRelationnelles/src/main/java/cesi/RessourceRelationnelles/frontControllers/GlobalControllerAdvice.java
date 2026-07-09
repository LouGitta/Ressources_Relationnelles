package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

/**
 * Advice global injecté avant chaque requête contrôleur.
 * Résout l'utilisateur connecté depuis le Principal Spring Security
 * et expose les attributs communs à toutes les vues Thymeleaf.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceDetector deviceDetector;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {

        model.addAttribute("isMobile", deviceDetector.isMobile(request));

        Principal principal = request.getUserPrincipal();
        boolean isConnected = principal != null;
        model.addAttribute("isConnected", isConnected);

        boolean isAdmin = false;
        boolean isModo  = false;

        if (isConnected) {
            // Le principal Spring Security contient l'email (configuré dans DbUserDetailsService)
            String login = principal.getName();
            Optional<User> userOpt = userService.getByEmail(login);
            if (userOpt.isEmpty()) {
                // Fallback username pour rétro-compatibilité
                userOpt = userService.getByUsername(login);
            }

            if (userOpt.isPresent()) {
                User currentUser = userOpt.get();
                // Rend l'utilisateur courant disponible sur TOUTES les pages
                model.addAttribute("currentUser", currentUser);

                Role role = currentUser.getRole();
                if (role == Role.ADMINISTRATOR || role == Role.SUPERADMIN) {
                    isAdmin = true;
                } else if (role == Role.MODERATOR) {
                    isModo = true;
                }
            }
        }

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isModo", isModo);
    }
}