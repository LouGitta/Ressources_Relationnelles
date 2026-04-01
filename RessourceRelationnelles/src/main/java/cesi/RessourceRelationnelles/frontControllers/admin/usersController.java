package cesi.RessourceRelationnelles.frontControllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.UserService;

@Controller
@RequestMapping("/admin/users")
public class usersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("listUsers", userService.getAll());
        return "admin/users";
    }
}