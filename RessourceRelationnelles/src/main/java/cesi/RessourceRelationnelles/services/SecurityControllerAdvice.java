package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice(basePackages = "cesi.RessourceRelationnelles.frontControllers")
public class SecurityControllerAdvice {

    private final UserContextService userContextService;

    @Autowired
    public SecurityControllerAdvice(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    @ModelAttribute
    public void addSecurityAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        boolean isConnected = userContextService.isConnected(principal);
        
        model.addAttribute("isConnected", isConnected);
        
        boolean isAdmin = false;
        boolean isModo = false;

        if (isConnected) {
            userContextService.getCurrentUser(principal).ifPresent(user -> {
                model.addAttribute("currentUser", user);
                
                // Idéalement, cette vérification de rôle pourrait aussi aller dans le UserContextService ou le modèle User
                Role role = user.getRole();
                model.addAttribute("isAdmin", role == Role.administrator || role == Role.super_admin);
                model.addAttribute("isModo", role == Role.moderator);
            });
        } else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("isModo", false);
        }
    }
}