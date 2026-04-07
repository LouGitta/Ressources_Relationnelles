package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateAccountController {
    @GetMapping("/app/create-account")
    public String afficherCreateAccount() {
        return "createAccount";
    }
}