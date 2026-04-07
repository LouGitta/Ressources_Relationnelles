package cesi.RessourceRelationnelles.frontControllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cesi.RessourceRelationnelles.models.Category;
import cesi.RessourceRelationnelles.services.CategoryService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("listCategories", categoryService.getAll());
        return "admin/categories";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categoryForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Category> categoryOpt = categoryService.getById(id); // Assure-toi d'avoir cette méthode dans ton
                                                                      // service
        if (categoryOpt.isPresent()) {
            model.addAttribute("category", categoryOpt.get());
            return "admin/categoryForm";
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            categoryService.delete(id);

            redirectAttrs.addFlashAttribute("successMessage", "La catégorie a été supprimée avec succès.");

        } catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Impossible de supprimer cette catégorie car elle est utilisée par une ou plusieurs ressources. Veuillez d'abord réassigner ces ressources.");
        }

        return "redirect:/admin/categories";
    }
}