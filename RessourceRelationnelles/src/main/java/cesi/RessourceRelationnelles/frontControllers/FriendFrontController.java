package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.FriendService;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Contrôleur pour gérer les demandes d'amis.
 * Permet d'envoyer, accepter et refuser des demandes d'amis.
 */
@Controller
public class FriendFrontController {

    private static final Logger logger = LoggerFactory.getLogger(FriendFrontController.class);

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    /**
     * Envoie une demande d'ami à un utilisateur.
     * La logique d'initialisation (statut pending, date) est déléguée à {@link FriendService#sendFriendRequest}.
     *
     * @param targetUserId ID de l'utilisateur cible
     * @param ressourceId  ID de la ressource (pour la redirection)
     * @param model        Le modèle pour la vue
     * @return Redirection vers la ressource
     */
    @PostMapping(Routes.ADD_FRIEND)
    public String sendFriendRequest(@PathVariable Integer targetUserId,
                                    @RequestParam Integer ressourceId,
                                    Model model) {
        logger.debug("Envoi d'une demande d'ami à l'utilisateur {}", targetUserId);

        User currentUser = requireAuthenticated(model);

        ValidationHelper.validatePositiveId(targetUserId, "targetUserId");
        ValidationHelper.validatePositiveId(ressourceId, "ressourceId");

        // Vérifier que l'utilisateur ne s'ajoute pas lui-même
        if (currentUser.getId().equals(targetUserId)) {
            logger.warn("L'utilisateur {} a tenté de s'ajouter en ami", currentUser.getId());
            return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
        }

        User targetUser = userService.getById(targetUserId)
                .orElseThrow(() -> {
                    logger.warn("Utilisateur cible {} non trouvé", targetUserId);
                    return ResourceNotFoundException.notFound("Utilisateur", targetUserId);
                });

        // Déléguer la création de la demande au service (statut + date gérés par le service)
        friendService.sendFriendRequest(currentUser, targetUser);

        logger.info("Demande d'ami envoyée de {} à {}", currentUser.getId(), targetUserId);
        return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
    }

    /**
     * Accepte une demande d'ami.
     *
     * @param id    ID de la relation d'amitié
     * @param model Le modèle pour la vue
     * @return Redirection vers le profil
     */
    @PostMapping(Routes.ACCEPT_FRIEND)
    public String acceptFriend(@PathVariable Integer id, Model model) {
        logger.debug("Acceptation de la demande d'ami {}", id);

        User currentUser = requireAuthenticated(model);
        ValidationHelper.validatePositiveId(id, "id");

        Friend friend = friendService.getById(id)
                .orElseThrow(() -> {
                    logger.warn("Relation d'amitié {} non trouvée", id);
                    return ResourceNotFoundException.notFound("Demande d'ami", id);
                });

        if (!friend.getUser2().getId().equals(currentUser.getId())) {
            logger.warn("L'utilisateur {} n'a pas le droit d'accepter la demande {}", currentUser.getId(), id);
            throw UnauthorizedException.forbidden("accepter cette demande d'ami");
        }

        friend.setStatus(cesi.RessourceRelationnelles.models.FriendStatus.accepted);
        friendService.save(friend);

        logger.info("Demande d'ami {} acceptée par l'utilisateur {}", id, currentUser.getId());
        return Routes.REDIRECT_PROFILE;
    }

    /**
     * Refuse une demande d'ami.
     *
     * @param id    ID de la relation d'amitié
     * @param model Le modèle pour la vue
     * @return Redirection vers le profil
     */
    @PostMapping(Routes.REJECT_FRIEND)
    public String rejectFriend(@PathVariable Integer id, Model model) {
        logger.debug("Refus de la demande d'ami {}", id);

        User currentUser = requireAuthenticated(model);
        ValidationHelper.validatePositiveId(id, "id");

        Friend friend = friendService.getById(id)
                .orElseThrow(() -> {
                    logger.warn("Relation d'amitié {} non trouvée", id);
                    return ResourceNotFoundException.notFound("Demande d'ami", id);
                });

        if (!friend.getUser2().getId().equals(currentUser.getId())) {
            logger.warn("L'utilisateur {} n'a pas le droit de refuser la demande {}", currentUser.getId(), id);
            throw UnauthorizedException.forbidden("refuser cette demande d'ami");
        }

        friendService.delete(id);

        logger.info("Demande d'ami {} refusée par l'utilisateur {}", id, currentUser.getId());
        return Routes.REDIRECT_PROFILE;
    }

    /**
     * Vérifie que l'utilisateur courant est authentifié.
     *
     * @param model Le modèle Spring MVC
     * @return L'utilisateur courant
     * @throws UnauthorizedException si l'utilisateur n'est pas connecté
     */
    private User requireAuthenticated(Model model) {
        User user = (User) model.getAttribute("currentUser");
        if (user == null) {
            logger.warn("Opération amis tentée sans authentification");
            throw UnauthorizedException.notAuthenticated();
        }
        return user;
    }
}