package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.PermissionService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour gérer les opérations sur les commentaires.
 * Gère l'ajout et la suppression de commentaires.
 */
@Controller
public class CommentFrontController {

    private static final Logger logger = LoggerFactory.getLogger(CommentFrontController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private PermissionService permissionService;

    /**
     * Ajoute un nouveau commentaire à une ressource.
     *
     * @param ressourceId L'ID de la ressource
     * @param content     Le contenu du commentaire
     * @param model       Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.ADD_COMMENT)
    public String addComment(
            @PathVariable("id") Integer ressourceId,
            @RequestParam("content") String content,
            Model model) {

        logger.info("Tentative d'ajout de commentaire pour la ressource ID: {}", ressourceId);

        User user = requireAuthenticated(model);

        ValidationHelper.validateNotBlank(content, "contenu du commentaire");
        ValidationHelper.validatePositiveId(ressourceId, "ressourceId");

        // Création et sauvegarde via le service métier (ResourceNotFoundException propagée si ressource absente)
        commentService.createComment(ressourceId, content, user);
        logger.info("Commentaire créé avec succès pour la ressource: {}", ressourceId);

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }

    /**
     * Supprime un commentaire si l'utilisateur est propriétaire ou modérateur.
     *
     * @param commentId   L'ID du commentaire à supprimer
     * @param ressourceId L'ID de la ressource associée
     * @param model       Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.DELETE_COMMENT)
    public String deleteComment(
            @PathVariable("id") Integer commentId,
            @RequestParam("ressourceId") Integer ressourceId,
            Model model) {

        logger.info("Tentative de suppression du commentaire ID: {} de la ressource ID: {}",
                    commentId, ressourceId);

        User user = requireAuthenticated(model);

        ValidationHelper.validatePositiveId(commentId, "commentId");
        ValidationHelper.validatePositiveId(ressourceId, "ressourceId");

        if (!permissionService.canDeleteComment(user.getId(), commentId)) {
            logger.warn("Utilisateur {} tente de supprimer un commentaire non autorisé", user.getId());
            throw UnauthorizedException.forbidden("Supprimer ce commentaire");
        }

        commentService.delete(commentId);
        logger.info("Commentaire {} supprimé avec succès par l'utilisateur {}", commentId, user.getId());

        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }

    /**
     * Vérifie que l'utilisateur courant est authentifié.
     *
     * @param model Le modèle Spring MVC
     * @return L'utilisateur courant
     * @throws UnauthorizedException si l'utilisateur n'est pas connecté
     */
    private User requireAuthenticated(Model model) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            logger.warn("Opération commentaire tentée sans authentification");
            throw UnauthorizedException.notAuthenticated();
        }
        return user;
    }
}