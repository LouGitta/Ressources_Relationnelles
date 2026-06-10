package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;
import cesi.RessourceRelationnelles.repositories.FriendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les relations d'amitié.
 * Centralise la logique métier liée aux amis avec logging.
 */
@Service
public class FriendService {

    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Autowired
    private FriendRepository friendRepository;

    /**
     * Récupère une relation d'amitié par son ID.
     *
     * @param id ID de la relation
     * @return Optional contenant la relation ou vide
     */
    public Optional<Friend> getById(Integer id) {
        logger.debug("Récupération de la relation d'amitié {}", id);
        return friendRepository.findById(id);
    }

    /**
     * Récupère tous les amis acceptés d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return liste des amis acceptés
     */
    public List<Friend> getAllAcceptedFriends(Integer userId) {
        logger.debug("Récupération des amis acceptés de l'utilisateur {}", userId);
        return friendRepository.findAllAcceptedByUserId(userId, FriendStatus.accepted);
    }

    /**
     * Récupère les demandes d'ami envoyées par un utilisateur (pending).
     *
     * @param user1 ID de l'utilisateur qui a envoyé les demandes
     * @return liste des demandes pending
     */
    public List<Friend> getByUser1(Integer user1) {
        logger.debug("Récupération des demandes envoyées par l'utilisateur {}", user1);
        return friendRepository.findByUser1_Id(user1, FriendStatus.pending);
    }

    /**
     * Récupère les demandes d'ami reçues par un utilisateur (pending).
     *
     * @param user2 ID de l'utilisateur qui a reçu les demandes
     * @return liste des demandes pending
     */
    public List<Friend> getByUser2(Integer user2) {
        logger.debug("Récupération des demandes reçues par l'utilisateur {}", user2);
        return friendRepository.findByUser2_Id(user2, FriendStatus.pending);
    }

    /**
     * Récupère toutes les relations (tous les statuts) d'un utilisateur.
     *
     * @param user1 ID de l'utilisateur
     * @return liste de toutes les relations
     */
    public List<Friend> getAllByUser1(Integer user1) {
        logger.debug("Récupération de toutes les relations de l'utilisateur {}", user1);
        return friendRepository.findAllByUser1_Id(user1);
    }

    /**
     * Sauvegarde une relation d'amitié.
     *
     * @param friend la relation à sauvegarder
     * @return la relation sauvegardée
     */
    @Transactional
    public Friend save(Friend friend) {
        logger.debug("Sauvegarde de la relation d'amitié entre {} et {}", 
            friend.getUser1().getId(), friend.getUser2().getId());
        return friendRepository.save(friend);
    }

    /**
     * Supprime une relation d'amitié.
     *
     * @param id ID de la relation à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.debug("Suppression de la relation d'amitié {}", id);
        friendRepository.deleteById(id);
    }

    /**
     * Compte le nombre total de relations d'amitié.
     *
     * @return nombre de relations
     */
    public long countAll() {
        return friendRepository.count();
    }

    /**
     * Envoie une demande d'ami d'un utilisateur vers un autre.
     * Initialise le statut à {@link FriendStatus#pending} et la date de création.
     *
     * @param sender   L'utilisateur qui envoie la demande
     * @param receiver L'utilisateur qui reçoit la demande
     * @return La relation d'amitié créée
     */
    @Transactional
    public Friend sendFriendRequest(
            cesi.RessourceRelationnelles.models.User sender,
            cesi.RessourceRelationnelles.models.User receiver) {
        logger.info("Envoi d'une demande d'ami de {} vers {}", sender.getId(), receiver.getId());
        Friend request = new Friend();
        request.setUser1(sender);
        request.setUser2(receiver);
        request.setStatus(FriendStatus.pending);
        request.setCreatedAt(java.time.LocalDateTime.now());
        return save(request);
    }

    /**
     * Vérifie si deux utilisateurs sont amis (relation acceptée).
     *
     * @param userId1 ID du premier utilisateur
     * @param userId2 ID du deuxième utilisateur
     * @return true si ils sont amis, false sinon
     */
    public boolean areFriends(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null) {
            return false;
        }
        if (userId1.equals(userId2)) {
            return true;
        }
        List<Friend> accepted = getAllAcceptedFriends(userId1);
        for (Friend f : accepted) {
            if (f.getUser1().getId().equals(userId2) || f.getUser2().getId().equals(userId2)) {
                return true;
            }
        }
        return false;
    }
}