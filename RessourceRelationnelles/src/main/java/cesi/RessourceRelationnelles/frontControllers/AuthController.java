package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/app/login")
    public String afficherAuth() {
        return "auth";
    }

    @GetMapping("/login")
    public String redirectLogin() {
        return "redirect:/app/login";
    }

}
