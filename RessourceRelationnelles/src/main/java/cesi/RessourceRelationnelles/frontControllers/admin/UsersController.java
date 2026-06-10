package cesi.RessourceRelationnelles.frontControllers.admin;

import java.util.List;
import java.util.Optional;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

/**
 * Contrôleur d'administration pour la gestion des utilisateurs.
 * Permet de lister, créer, modifier et supprimer des comptes utilisateurs.
 */
@Controller
@RequestMapping("/admin/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Affiche la liste des utilisateurs avec filtres optionnels.
     *
     * @param keyword  Mot-clé de recherche (nom d'utilisateur ou email)
     * @param role     Filtre par rôle
     * @param isActive Filtre par statut d'activation
     * @param model    Le modèle pour la vue
     * @return La vue "admin/users"
     */
    @GetMapping
    public String listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean isActive,
            Model model) {

        logger.debug("Listage des utilisateurs — keyword={}, role={}, isActive={}", keyword, role, isActive);

        List<User> users = userService.searchAndFilter(keyword, role, isActive);
        model.addAttribute("listUsers", users);
        model.addAttribute("roles", Role.values());
        model.addAttribute("selectedKeyword", keyword);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedIsActive", isActive);

        logger.info("Affichage de {} utilisateurs", users.size());
        return "admin/users";
    }

    /**
     * Affiche le formulaire de création d'un nouvel utilisateur.
     *
     * @param model     Le modèle pour la vue
     * @param principal L'utilisateur connecté
     * @return La vue "admin/userForm"
     */
    @GetMapping("/new")
    public String showCreateForm(Model model, Principal principal) {
        logger.debug("Affichage du formulaire de création d'utilisateur");
        UserFormDTO newUser = new UserFormDTO();
        newUser.setActive(true);
        model.addAttribute("user", newUser);
        prepareRoleModel(model, principal);
        return "admin/userForm";
    }

    /**
     * Affiche le formulaire d'édition d'un utilisateur existant.
     *
     * @param id        ID de l'utilisateur à modifier
     * @param model     Le modèle pour la vue
     * @param principal L'utilisateur connecté
     * @return La vue "admin/userForm" ou redirection si non trouvé
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, Principal principal) {
        logger.debug("Affichage du formulaire d'édition pour l'utilisateur {}", id);
        Optional<User> userOpt = userService.getById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", DtoMapper.toFormDTO(userOpt.get()));
            prepareRoleModel(model, principal);
            return "admin/userForm";
        }
        logger.warn("Utilisateur {} non trouvé — redirection vers la liste", id);
        return "redirect:/admin/users";
    }

    /**
     * Sauvegarde un utilisateur (création ou mise à jour).
     * En création, le mot de passe est haché en BCrypt.
     * En modification, le mot de passe existant est conservé si le champ est vide.
     *
     * @param userFormDTO   Le DTO de l'utilisateur soumis
     * @param bindingResult Résultat de la validation
     * @param principal     L'utilisateur connecté
     * @param model         Le modèle pour la vue
     * @return Redirection vers la liste ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("user") UserFormDTO userFormDTO,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (bindingResult.hasErrors()) {
            logger.warn("Erreurs de validation lors de la sauvegarde de l'utilisateur");
            prepareRoleModel(model, principal);
            return "admin/userForm";
        }

        User user = DtoMapper.toEntity(userFormDTO);

        if (user.getId() == null) {
            // Création : hachage obligatoire du mot de passe
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                logger.debug("Mot de passe haché pour le nouvel utilisateur");
            }
            logger.info("Création d'un nouvel utilisateur: {}", user.getUsername());
        } else {
            // Mise à jour : conserver les champs immuables
            User existingUser = userService.getById(user.getId()).orElse(null);
            if (existingUser != null) {
                user.setCreatedAt(existingUser.getCreatedAt());
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    // Aucun nouveau mot de passe → conserver l'ancien
                    user.setPassword(existingUser.getPassword());
                } else {
                    // Nouveau mot de passe fourni → hacher
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    logger.debug("Mot de passe mis à jour et haché pour l'utilisateur {}", user.getId());
                }
            }
            logger.info("Mise à jour de l'utilisateur {}", user.getId());
        }

        userService.save(user);
        return "redirect:/admin/users";
    }

    /**
     * Supprime un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur à supprimer
     * @return Redirection vers la liste
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        logger.warn("Suppression de l'utilisateur {}", id);
        userService.delete(id);
        return "redirect:/admin/users";
    }

    /**
     * Prépare les attributs du modèle liés aux rôles pour le formulaire.
     *
     * @param model     Le modèle pour la vue
     * @param principal L'utilisateur connecté
     */
    private void prepareRoleModel(Model model, Principal principal) {
        String currentRole = userContextService.getCurrentUser(principal)
                .map(u -> u.getRole().name())
                .orElse("CITIZEN");
        model.addAttribute("currentUserRole", currentRole);
        model.addAttribute("roles", Role.values());
    }
}