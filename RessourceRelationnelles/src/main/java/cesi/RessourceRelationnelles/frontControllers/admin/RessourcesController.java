package cesi.RessourceRelationnelles.frontControllers.admin;

import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.RelationService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.dtos.RessourceFormDTO;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.TypeService;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.services.UserContextService;
import cesi.RessourceRelationnelles.utils.DtoMapper;

import java.security.Principal;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

/**
 * Contrôleur d'administration pour la gestion des ressources.
 * Permet de lister, créer, modifier et supprimer des ressources.
 */
@Controller
@RequestMapping("/admin/ressources")
public class RessourcesController {

    private static final Logger logger = LoggerFactory.getLogger(RessourcesController.class);

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

    /**
     * Affiche la liste des ressources avec filtres avancés.
     *
     * @param title      Filtre par titre
     * @param categoryId Filtre par catégorie
     * @param relationId Filtre par relation
     * @param typeId     Filtre par type
     * @param visibility Filtre par visibilité
     * @param status     Filtre par statut
     * @param model      Le modèle pour la vue
     * @return La vue "admin/ressources"
     */
    @GetMapping
    public String listItems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer relationId,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Visibility visibility,
            @RequestParam(required = false) RessourceStatus status,
            Model model) {

        logger.debug("Listage admin des ressources — title={}, categoryId={}, status={}", title, categoryId, status);

        List<Ressource> ressources = ressourceService.searchAdmin(title, categoryId, relationId, typeId, visibility, status);
        model.addAttribute("listRessources", ressources);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("relations", relationService.getAll());
        model.addAttribute("types", typeService.getAll());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("statuses", RessourceStatus.values());
        model.addAttribute("selectedTitle", title);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedRelationId", relationId);
        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("selectedVisibility", visibility);
        model.addAttribute("selectedStatus", status);

        logger.info("Affichage de {} ressources (admin)", ressources.size());
        return "admin/ressources";
    }

    /**
     * Affiche le formulaire de création d'une nouvelle ressource.
     *
     * @param model Le modèle pour la vue
     * @return La vue "admin/ressourceForm"
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.debug("Affichage du formulaire de création de ressource (admin)");
        RessourceFormDTO newRessource = new RessourceFormDTO();
        newRessource.setStatus(RessourceStatus.pending);
        model.addAttribute("ressource", newRessource);
        prepareFormModel(model);
        return "admin/ressourceForm";
    }

    /**
     * Affiche le formulaire d'édition d'une ressource existante.
     *
     * @param id    ID de la ressource à modifier
     * @param model Le modèle pour la vue
     * @return La vue "admin/ressourceForm" ou redirection si non trouvée
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        logger.debug("Affichage du formulaire d'édition pour la ressource {}", id);
        Optional<Ressource> ressourceOpt = ressourceService.getById(id);
        if (ressourceOpt.isPresent()) {
            model.addAttribute("ressource", DtoMapper.toFormDTO(ressourceOpt.get()));
            prepareFormModel(model);
            return "admin/ressourceForm";
        }
        logger.warn("Ressource {} non trouvée — redirection vers la liste", id);
        return "redirect:/admin/ressources";
    }

    /**
     * Sauvegarde une ressource (création ou mise à jour).
     * En création, le créateur est résolu depuis le Principal Spring Security.
     *
     * @param ressourceDTO  DTO de la ressource soumis
     * @param bindingResult Résultat de la validation
     * @param principal     L'utilisateur connecté
     * @param model         Le modèle pour la vue
     * @return Redirection vers la liste ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/save")
    public String saveRessource(
            @Valid @ModelAttribute("ressource") RessourceFormDTO ressourceDTO,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (bindingResult.hasErrors()) {
            logger.warn("Erreurs de validation lors de la sauvegarde de la ressource admin");
            prepareFormModel(model);
            return "admin/ressourceForm";
        }

        Ressource ressource = DtoMapper.toEntity(ressourceDTO);

        if (ressource.getId() == null) {
            // Création : résolution sécurisée du créateur
            User creator = userContextService.getCurrentUser(principal)
                    .orElseThrow(() -> UnauthorizedException.notAuthenticated());
            ressource.setUser(creator);
            ressource.setCreatedAt(java.time.LocalDateTime.now());
            ressource.setViews(0);
            logger.info("Création d'une nouvelle ressource '{}' par l'admin {}", ressource.getTitle(), creator.getId());
        } else {
            // Mise à jour : conserver les champs immuables de l'entité existante
            Ressource existing = ressourceService.getById(ressource.getId())
                    .orElseThrow(() -> ResourceNotFoundException.notFound("Ressource", ressource.getId()));
            ressource.setUser(existing.getUser());
            ressource.setCreatedAt(existing.getCreatedAt());
            ressource.setViews(existing.getViews());
            logger.info("Mise à jour de la ressource {} par l'admin", ressource.getId());
        }

        ressourceService.save(ressource);
        return "redirect:/admin/ressources";
    }

    /**
     * Supprime une ressource par son ID.
     *
     * @param id ID de la ressource à supprimer
     * @return Redirection vers la liste
     */
    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id) {
        logger.warn("Suppression de la ressource {} (admin)", id);
        ressourceService.delete(id);
        return "redirect:/admin/ressources";
    }

    /**
     * Prépare les listes référentielles communes au formulaire de ressource.
     * Centralise le chargement pour éviter la duplication (DRY).
     *
     * @param model Le modèle pour la vue
     */
    private void prepareFormModel(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("types", typeService.getAll());
        model.addAttribute("relations", relationService.getAll());
        model.addAttribute("visibilities", Visibility.values());
        model.addAttribute("statuses", RessourceStatus.values());
    }
}