import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cesi.RessourceRelationnelles.services.RessourceService;

@Controller
@RequestMapping("/admin/items")
public class AdminItemController {

    @Autowired
    private RessourceService ressourceService;

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("items", ressourceService.getAll());
        return "admin-items";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id) {
        ressourceService.delete(id);
        return "redirect:/admin/items";
    }
}