package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {
    
    @GetMapping("/app/contact")
    public String afficherContact() {
        return "contact";
    }
}