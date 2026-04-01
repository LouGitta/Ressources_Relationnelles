package cesi.RessourceRelationnelles.frontControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class legalMentionController {
    @GetMapping("/app/legalMention")
    public String afficherMentionsLegales() {
        return "legalMention";
    }
}
