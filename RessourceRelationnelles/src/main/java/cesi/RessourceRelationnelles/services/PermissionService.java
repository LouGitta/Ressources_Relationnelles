package cesi.RessourceRelationnelles.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service centralisé pour la gestion des permissions et des autorisations d'accès.
 * Fournit une interface unifiée pour vérifier les droits des utilisateurs sur les ressources et les commentaires.
 */
@Service
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RessourceService ressourceService;

    @Autowired
    private CommentService commentService;

    /**
     * Vérifie si un utilisateur possède des droits de modérateur ou supérieurs.
     *
     * @param userId ID de l'utilisateur à vérifier
     * @return true si l'utilisateur est modérateur ou supérieur, false sinon
     */
    public boolean isModeratorOrAbove(Integer userId) {
        if (userId == null) {
            return false;
        }
        boolean result = userService.isModerator(userId);
        logger.debug("Vérification rôle Modérateur pour l'utilisateur {}: {}", userId, result);
        return result;
    }

    /**
     * Vérifie si un utilisateur peut consulter une ressource spécifique.
     *
     * @param userId ID de l'utilisateur qui fait la demande (peut être null si anonyme)
     * @param ressourceId ID de la ressource à consulter
     * @return true si l'accès est autorisé, false sinon
     */
    public boolean canViewRessource(Integer userId, Integer ressourceId) {
        if (ressourceId == null) {
            return false;
        }
        boolean result = ressourceService.canViewRessource(userId, ressourceId);
        logger.debug("Vérification accès en lecture de la ressource {} par l'utilisateur {}: {}", 
            ressourceId, userId, result);
        return result;
    }

    /**
     * Vérifie si un utilisateur a le droit de modifier ou de supprimer une ressource.
     *
     * @param userId ID de l'utilisateur
     * @param ressourceId ID de la ressource
     * @return true si l'utilisateur est propriétaire ou modérateur, false sinon
     */
    public boolean canModifyRessource(Integer userId, Integer ressourceId) {
        if (userId == null || ressourceId == null) {
            return false;
        }
        boolean result = ressourceService.isOwnerOrModerator(userId, ressourceId);
        logger.debug("Vérification permission de modification sur la ressource {} pour l'utilisateur {}: {}", 
            ressourceId, userId, result);
        return result;
    }

    /**
     * Vérifie si un utilisateur a le droit de supprimer un commentaire.
     *
     * @param userId ID de l'utilisateur
     * @param commentId ID du commentaire
     * @return true si l'utilisateur est l'auteur ou modérateur, false sinon
     */
    public boolean canDeleteComment(Integer userId, Integer commentId) {
        if (userId == null || commentId == null) {
            return false;
        }
        boolean result = commentService.canDeleteComment(userId, commentId);
        logger.debug("Vérification permission de suppression sur le commentaire {} pour l'utilisateur {}: {}", 
            commentId, userId, result);
        return result;
    }
}
