package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CguController {
    @GetMapping("/cgu")
    public String afficherCgu() {
        return "cgu";
    }
}
