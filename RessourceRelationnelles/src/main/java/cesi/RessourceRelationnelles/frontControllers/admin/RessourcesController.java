package cesi.RessourceRelationnelles.frontControllers.admin;

import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RelationService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.dtos.RessourceFormDTO;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.TypeService;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.services.UserContextService;
import java.security.Principal;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/admin/ressources")
public class RessourcesController {

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserContextService userContextService;

    @GetMapping
    public String listItems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer relationId,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Visibility visibility,
            @RequestParam(required = false) RessourceStatus status,
            Model model) {

        List<Ressource> ressources = ressourceService.searchAdmin(title, categoryId, relationId, typeId, visibility,
                status);
        model.addAttribute("listRessources", ressources);

        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("relations", relationService.getAll()); // NOUVEAU
        model.addAttribute("types", typeService.getAll()); // NOUVEAU
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("statuses", RessourceStatus.values());

        model.addAttribute("selectedTitle", title);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedRelationId", relationId);
        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("selectedVisibility", visibility);
        model.addAttribute("selectedStatus", status);

        return "admin/ressources";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        RessourceFormDTO newRessource = new RessourceFormDTO();
        newRessource.setStatus(RessourceStatus.pending);

        model.addAttribute("ressource", newRessource);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("types", typeService.getAll());
        model.addAttribute("relations", relationService.getAll());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("statuses", RessourceStatus.values());

        return "admin/ressourceForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Ressource> ressourceOpt = ressourceService.getById(id);

        if (ressourceOpt.isPresent()) {
            model.addAttribute("ressource", cesi.RessourceRelationnelles.utils.DtoMapper.toFormDTO(ressourceOpt.get()));

            // Il faut envoyer TOUTES les listes pour les menus déroulants
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("types", typeService.getAll());
            model.addAttribute("relations", relationService.getAll());
            model.addAttribute("visibilities", Visibility.values());
            model.addAttribute("statuses", RessourceStatus.values());
            return "admin/ressourceForm";
        }
        return "redirect:/admin/ressources";
    }

    @PostMapping("/save")
    public String saveRessource(
            @Valid @ModelAttribute("ressource") RessourceFormDTO ressourceDTO, 
            BindingResult bindingResult, 
            Principal principal, 
            Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("types", typeService.getAll());
            model.addAttribute("relations", relationService.getAll());
            model.addAttribute("visibilities", Visibility.values());
            model.addAttribute("statuses", RessourceStatus.values());
            return "admin/ressourceForm";
        }

        Ressource ressource = cesi.RessourceRelationnelles.utils.DtoMapper.toEntity(ressourceDTO);

        if (ressource.getId() == null) {
            User creator = userContextService.getCurrentUser(principal).orElse(null);
            ressource.setUser(creator);
            ressource.setCreatedAt(java.time.LocalDateTime.now());
            ressource.setViews(0);
        } else {
            Optional<Ressource> existingOpt = ressourceService.getById(ressource.getId());

            if (existingOpt.isPresent()) {
                Ressource existing = existingOpt.get();
                ressource.setUser(existing.getUser());
                ressource.setCreatedAt(existing.getCreatedAt());
                ressource.setViews(existing.getViews());
            }
        }

        ressourceService.save(ressource);

        return "redirect:/admin/ressources";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id) {
        ressourceService.delete(id);
        return "redirect:/admin/ressources";
    }
}