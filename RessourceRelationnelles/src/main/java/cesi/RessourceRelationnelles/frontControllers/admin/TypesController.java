package cesi.RessourceRelationnelles.frontControllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.services.TypeService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/types")
public class TypesController {
    @Autowired
    private TypeService typeService;

    @GetMapping
    public String listTypes(Model model) {
        model.addAttribute("listTypes", typeService.getAll());
        return "admin/types";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("type", new Type());
        return "admin/typeForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Type> typeOpt = typeService.getById(id);
        if (typeOpt.isPresent()) {
            model.addAttribute("type", typeOpt.get());
            return "admin/typeForm";
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/save")
    public String saveType(@ModelAttribute("type") Type type) {
        typeService.save(type);
        return "redirect:/admin/types";
    }
}
