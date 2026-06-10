package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException;
import cesi.RessourceRelationnelles.exceptions.UnauthorizedException;
import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.FriendService;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import cesi.RessourceRelationnelles.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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

        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        // Validation
        ValidationHelper.validatePositiveId(targetUserId, "targetUserId");
        ValidationHelper.validatePositiveId(ressourceId, "ressourceId");

        Optional<User> targetOpt = userService.getById(targetUserId);
        if (!targetOpt.isPresent()) {
            logger.warn("Utilisateur cible {} non trouvé", targetUserId);
            throw ResourceNotFoundException.notFound("Utilisateur", targetUserId);
        }

        User targetUser = targetOpt.get();

        // Vérifier que l'utilisateur ne s'ajoute pas lui-même
        if (currentUser.getId().equals(targetUserId)) {
            logger.warn("L'utilisateur {} a tenté de s'ajouter en ami", currentUser.getId());
            return "redirect:" + Routes.RESSOURCE_DETAIL.replace("{id}", ressourceId.toString());
        }

        // Créer la demande
        Friend requestFriend = new Friend();
        requestFriend.setUser1(currentUser);
        requestFriend.setUser2(targetUser);
        requestFriend.setStatus(FriendStatus.pending);
        requestFriend.setCreatedAt(LocalDateTime.now());

        friendService.save(requestFriend);

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

        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        ValidationHelper.validatePositiveId(id, "id");

        Optional<Friend> friendOpt = friendService.getById(id);
        if (!friendOpt.isPresent()) {
            logger.warn("Relation d'amitié {} non trouvée", id);
            throw ResourceNotFoundException.notFound("Demande d'ami", id);
        }

        Friend friend = friendOpt.get();

        // Vérifier que l'utilisateur est le destinataire de la demande
        if (!friend.getUser2().getId().equals(currentUser.getId())) {
            logger.warn("L'utilisateur {} n'a pas le droit d'accepter cette demande", currentUser.getId());
            throw UnauthorizedException.forbidden("accepter cette demande d'ami");
        }

        friend.setStatus(FriendStatus.accepted);
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

        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("Utilisateur non authentifié - redirection");
            throw UnauthorizedException.notAuthenticated();
        }

        ValidationHelper.validatePositiveId(id, "id");

        Optional<Friend> friendOpt = friendService.getById(id);
        if (!friendOpt.isPresent()) {
            logger.warn("Relation d'amitié {} non trouvée", id);
            throw ResourceNotFoundException.notFound("Demande d'ami", id);
        }

        Friend friend = friendOpt.get();

        // Vérifier que l'utilisateur est le destinataire de la demande
        if (!friend.getUser2().getId().equals(currentUser.getId())) {
            logger.warn("L'utilisateur {} n'a pas le droit de refuser cette demande", currentUser.getId());
            throw UnauthorizedException.forbidden("refuser cette demande d'ami");
        }

        friendService.delete(id);

        logger.info("Demande d'ami {} refusée par l'utilisateur {}", id, currentUser.getId());
        return Routes.REDIRECT_PROFILE;
    }
}