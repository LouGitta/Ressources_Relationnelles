package cesi.RessourceRelationnelles.frontControllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.services.RelationService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/relations")
public class RelationsController {
    @Autowired
    private RelationService relationService;

    @GetMapping
    public String listRelations(Model model) {
        model.addAttribute("listRelations", relationService.getAll());
        return "admin/relations";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("relation", new Relation());
        return "admin/relationForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Relation> typeOpt = relationService.getById(id);
        if (typeOpt.isPresent()) {
            model.addAttribute("relation", typeOpt.get());
            return "admin/relationForm";
        }
        return "redirect:/admin/relations";
    }

    @PostMapping("/save")
    public String saveType(@ModelAttribute("relation") Relation relation) {
        relationService.save(relation);
        return "redirect:/admin/relations";
    }
}
