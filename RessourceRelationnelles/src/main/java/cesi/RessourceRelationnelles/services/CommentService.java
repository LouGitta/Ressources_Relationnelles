package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.repositories.CommentRepository;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les commentaires.
 * Centralise la logique métier liée aux commentaires avec validation et logging.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RessourceService ressourceService;

    /**
     * Récupère tous les commentaires.
     *
     * @return liste de tous les commentaires
     */
    public List<Comment> getAll() {
        logger.debug("Récupération de tous les commentaires");
        return commentRepository.findAll();
    }

    /**
     * Récupère les commentaires d'une ressource.
     *
     * @param ressourceId ID de la ressource
     * @return liste des commentaires
     */
    public List<Comment> getByRessource(Integer ressourceId) {
        logger.debug("Récupération des commentaires pour la ressource {}", ressourceId);
        return commentRepository.findByRessourceId(ressourceId);
    }

    /**
     * Récupère les commentaires d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return liste des commentaires
     */
    public List<Comment> getByUser(Integer userId) {
        logger.debug("Récupération des commentaires de l'utilisateur {}", userId);
        return commentRepository.findByUserId(userId);
    }

    /**
     * Récupère les réponses à un commentaire.
     *
     * @param parentId ID du commentaire parent
     * @return liste des réponses
     */
    public List<Comment> getByParent(Integer parentId) {
        logger.debug("Récupération des réponses au commentaire {}", parentId);
        return commentRepository.findByParentId(parentId);
    }

    /**
     * Récupère un commentaire par son ID.
     *
     * @param id ID du commentaire
     * @return Optional contenant le commentaire ou vide
     */
    public Optional<Comment> getById(Integer id) {
        logger.debug("Récupération du commentaire {}", id);
        return commentRepository.findById(id);
    }

    /**
     * Sauvegarde un commentaire.
     *
     * @param comment le commentaire à sauvegarder
     * @return le commentaire sauvegardé
     */
    @Transactional
    public Comment save(Comment comment) {
        logger.debug("Sauvegarde du commentaire par l'utilisateur {}", comment.getUser().getId());
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createComment(Integer ressourceId, String content, User user) {
        logger.debug("Création d'un nouveau commentaire sur la ressource {} par l'utilisateur {}", ressourceId, user.getId());
        Ressource ressource = ressourceService.getById(ressourceId)
                .orElseThrow(() -> cesi.RessourceRelationnelles.exceptions.ResourceNotFoundException.notFound("Ressource", ressourceId));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now());
        comment.setUser(user);
        comment.setRessource(ressource);

        return save(comment);
    }

    /**
     * Supprime un commentaire.
     *
     * @param id ID du commentaire à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.debug("Suppression du commentaire {}", id);
        commentRepository.deleteById(id);
    }

    /**
     * Vérifie si l'utilisateur peut supprimer un commentaire.
     * Un commentaire peut être supprimé par son auteur ou par un modérateur.
     *
     * @param userId ID de l'utilisateur
     * @param commentId ID du commentaire
     * @return true si l'utilisateur peut supprimer, false sinon
     */
    public boolean canDeleteComment(Integer userId, Integer commentId) {
        logger.debug("Vérification des permissions de suppression pour le commentaire {} par l'utilisateur {}", 
            commentId, userId);
        return isUserAuthorOrModerator(userId, commentId);
    }

    /**
     * Vérifie si l'utilisateur est l'auteur du commentaire ou un modérateur.
     *
     * @param userId ID de l'utilisateur
     * @param commentId ID du commentaire
     * @return true si l'utilisateur est l'auteur ou modérateur, false sinon
     */
    public boolean isUserAuthorOrModerator(Integer userId, Integer commentId) {
        if (userId == null || commentId == null) {
            return false;
        }
        Optional<Comment> commentOpt = getById(commentId);
        if (commentOpt.isEmpty()) {
            logger.warn("Commentaire {} non trouvé", commentId);
            return false;
        }
        Comment comment = commentOpt.get();
        
        // Est-ce l'auteur ?
        if (comment.getUser().getId().equals(userId)) {
            logger.debug("Utilisateur {} est l'auteur du commentaire {}", userId, commentId);
            return true;
        }
        
        // Est-ce un modérateur ?
        boolean isMod = userService.isModerator(userId);
        if (isMod) {
            logger.debug("Utilisateur {} est modérateur ou supérieur, autorisé pour le commentaire {}", userId, commentId);
        }
        return isMod;
    }

    /**
     * Compte le nombre total de commentaires.
     *
     * @return nombre de commentaires
     */
    public long countAll() {
        logger.debug("Comptage total des commentaires");
        return commentRepository.count();
    }
}