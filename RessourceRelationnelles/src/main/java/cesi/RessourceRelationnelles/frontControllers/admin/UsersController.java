package cesi.RessourceRelationnelles.frontControllers.admin;

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

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;

@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean isActive,
            Model model) {

        List<User> users = userService.searchAndFilter(keyword, role, isActive);
        model.addAttribute("listUsers", users);

        model.addAttribute("roles", Role.values());

        model.addAttribute("selectedKeyword", keyword);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedIsActive", isActive);

        return "admin/users";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        User newUser = new User();
        newUser.setActive(true);
        model.addAttribute("user", newUser);
        model.addAttribute("roles", Role.values());
        prepareRoleModel(model);
        return "admin/userForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.getById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", Role.values());
            prepareRoleModel(model);
            return "admin/userForm";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getId() == null) {
            // user.setCreatedAt(java.time.LocalDateTime.now());
        } else {
            User existingUser = userService.getById(user.getId()).orElse(null);
            if (existingUser != null) {
                user.setCreatedAt(existingUser.getCreatedAt());
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                }
            }
        }

        userService.save(user);
        return "redirect:/admin/users";
    }

    private void prepareRoleModel(Model model) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String currentRole = auth.getAuthorities().stream()
        // .map(r -> r.getAuthority())
        // .findFirst().orElse("ROLE_citizen");

        // DEV
        String currentRole = "super_admin";
        model.addAttribute("currentUserRole", currentRole);
        model.addAttribute("roles", Role.values());
    }
}