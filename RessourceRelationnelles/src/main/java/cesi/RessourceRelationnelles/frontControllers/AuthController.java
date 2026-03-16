package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/app/auth")
    public String afficherAuth() {
        return "auth";
    }
}
