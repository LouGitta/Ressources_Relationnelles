package cesi.RessourceRelationnelles.frontControllers.admin;

import java.util.List;
import java.util.Optional;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.dtos.UserFormDTO;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.services.UserContextService;
import cesi.RessourceRelationnelles.utils.DtoMapper;

@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserContextService userContextService;

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
    public String showCreateForm(Model model, Principal principal) {
        UserFormDTO newUser = new UserFormDTO();
        newUser.setActive(true);
        model.addAttribute("user", newUser);
        model.addAttribute("roles", Role.values());
        prepareRoleModel(model, principal);
        return "admin/userForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, Principal principal) {
        Optional<User> userOpt = userService.getById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", DtoMapper.toFormDTO(userOpt.get()));
            model.addAttribute("roles", Role.values());
            prepareRoleModel(model, principal);
            return "admin/userForm";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("user") UserFormDTO userFormDTO,
            BindingResult bindingResult,
            Principal principal,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            prepareRoleModel(model, principal);
            return "admin/userForm";
        }

        User user = DtoMapper.toEntity(userFormDTO);
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

    private void prepareRoleModel(Model model, Principal principal) {
        String currentRole = userContextService.getCurrentUser(principal)
                .map(u -> u.getRole().name())
                .orElse("CITIZEN");
        model.addAttribute("currentUserRole", currentRole);
        model.addAttribute("roles", Role.values());
    }
}