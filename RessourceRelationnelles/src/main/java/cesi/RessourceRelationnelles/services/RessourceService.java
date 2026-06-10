package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.repositories.RessourceRepository;
import cesi.RessourceRelationnelles.utils.AuthorizationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les ressources.
 * Centralise la logique métier liée aux ressources avec validation et logging.
 */
@Service
public class RessourceService {

    private static final Logger logger = LoggerFactory.getLogger(RessourceService.class);

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ActivityParticipantRepository activityParticipantRepository;

    /**
     * Récupère toutes les ressources.
     *
     * @return liste de toutes les ressources
     */
    public List<Ressource> getAll() {
        logger.debug("Récupération de toutes les ressources");
        return ressourceRepository.findAll();
    }

    /**
     * Récupère une ressource par son ID.
     *
     * @param id ID de la ressource
     * @return Optional contenant la ressource ou vide
     */
    public Optional<Ressource> getById(Integer id) {
        logger.debug("Récupération de la ressource {}", id);
        return ressourceRepository.findById(id);
    }

    /**
     * Sauvegarde une ressource.
     *
     * @param ressource la ressource à sauvegarder
     * @return la ressource sauvegardée
     */
    @Transactional
    public Ressource save(Ressource ressource) {
        logger.debug("Sauvegarde de la ressource '{}' par l'utilisateur {}", 
            ressource.getTitle(), ressource.getUser().getId());
        return ressourceRepository.save(ressource);
    }

    /**
     * Supprime une ressource.
     *
     * @param id ID de la ressource à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.debug("Suppression de la ressource {}", id);
        ressourceRepository.deleteById(id);
    }

    /**
     * Crée et persiste une nouvelle ressource avec les valeurs initiales métier.
     * Centralise : statut initial à {@code pending}, date de création et compteur de vues.
     *
     * @param title      Titre de la ressource
     * @param content    Contenu de la ressource
     * @param visibility Visibilité choisie par l'utilisateur
     * @param author     Auteur de la ressource
     * @param category   Catégorie sélectionnée (peut être null)
     * @param type       Type sélectionné (peut être null)
     * @param relation   Relation sélectionnée (peut être null)
     * @return La ressource persistée
     */
    @Transactional
    public Ressource create(String title, String content, Visibility visibility,
            User author,
            cesi.RessourceRelationnelles.models.Category category,
            cesi.RessourceRelationnelles.models.Type type,
            cesi.RessourceRelationnelles.models.Relation relation) {
        logger.info("Création d'une nouvelle ressource '{}' par l'utilisateur {}", title, author.getId());
        Ressource ressource = new Ressource();
        ressource.setTitle(title.trim());
        ressource.setContent(content.trim());
        ressource.setVisibility(visibility);
        ressource.setStatus(RessourceStatus.pending);
        ressource.setCreatedAt(java.time.LocalDateTime.now());
        ressource.setViews(0);
        ressource.setUser(author);
        ressource.setCategory(category);
        ressource.setType(type);
        ressource.setRelation(relation);
        return save(ressource);
    }

    /**
     * Récupère les 10 ressources les plus récentes publiées.
     *
     * @return liste des ressources récentes
     */
    public List<Ressource> getRecentRessources() {
        logger.debug("Récupération des ressources récentes publiées");
        return ressourceRepository.findTop10ByStatusOrderByCreatedAtDesc(RessourceStatus.published);
    }

    /**
     * Recherche et filtre les ressources.
     *
     * @param title       titre à rechercher (optionnel)
     * @param categoryId  ID de la catégorie (optionnel)
     * @param visibility  visibilité (optionnel)
     * @param status      statut des ressources
     * @return liste des ressources filtrées
     */
    public List<Ressource> searchAndFilter(String title, Integer categoryId, Visibility visibility,
            RessourceStatus status) {
        logger.debug("Recherche ressources: title={}, categoryId={}, visibility={}, status={}", 
            title, categoryId, visibility, status);
        
        if (title != null && title.trim().isEmpty()) {
            title = null;
        }
        
        List<Ressource> results = ressourceRepository.findWithFilters(title, categoryId, visibility, status);
        logger.info("Recherche trouvée: {} ressources", results.size());
        return results;
    }

    /**
     * Récupère les ressources d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return liste des ressources
     */
    public List<Ressource> getByUser(Integer userId) {
        logger.debug("Récupération des ressources de l'utilisateur {}", userId);
        return ressourceRepository.findByUser_Id(userId);
    }

    /**
     * Compte le nombre total de ressources.
     *
     * @return nombre de ressources
     */
    public long countAll() {
        logger.debug("Comptage total des ressources");
        return ressourceRepository.count();
    }

    /**
     * Compte les ressources par catégorie.
     *
     * @return liste des statistiques par catégorie
     */
    public List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countRessourcesByCategory() {
        logger.debug("Comptage des ressources par catégorie");
        return ressourceRepository.countRessourcesByCategory();
    }

    /**
     * Compte les ressources par statut.
     *
     * @return liste des statistiques par statut
     */
    public List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countByStatus() {
        logger.debug("Comptage des ressources par statut");
        return ressourceRepository.countRessourcesByStatus();
    }

    /**
     * Compte les ressources par visibilité.
     *
     * @return liste des statistiques par visibilité
     */
    public List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countByVisibility() {
        logger.debug("Comptage des ressources par visibilité");
        return ressourceRepository.countRessourcesByVisibility();
    }

    /**
     * Récupère la liste des participants à une activité.
     *
     * @param ressourceId ID de la ressource (activité)
     * @return liste des participants
     */
    public List<ActivityParticipant> getParticipants(Integer ressourceId) {
        logger.debug("Récupération des participants pour la ressource {}", ressourceId);
        return activityParticipantRepository.findByRessource_Id(ressourceId);
    }

    /**
     * Vérifie si un utilisateur participe à une activité.
     *
     * @param ressourceId ID de la ressource (activité)
     * @param userId      ID de l'utilisateur
     * @return true si l'utilisateur participe
     */
    public boolean isUserParticipating(Integer ressourceId, Integer userId) {
        logger.debug("Vérification si l'utilisateur {} participe à la ressource {}", userId, ressourceId);
        return activityParticipantRepository.findByRessource_IdAndUser_Id(ressourceId, userId).isPresent();
    }

    /**
     * Inscrit un utilisateur à une activité s'il n'est pas déjà participant.
     *
     * @param ressource   La ressource (activité)
     * @param user        L'utilisateur qui rejoint l'activité
     */
    @Transactional
    public void joinActivity(Ressource ressource, User user) {
        if (activityParticipantRepository.findByRessource_IdAndUser_Id(ressource.getId(), user.getId()).isEmpty()) {
            ActivityParticipant participant = new ActivityParticipant();
            participant.setRessource(ressource);
            participant.setUser(user);
            participant.setJoinedAt(java.time.LocalDateTime.now());
            activityParticipantRepository.save(participant);
            logger.info("L'utilisateur {} a rejoint l'activité {}", user.getId(), ressource.getId());
        } else {
            logger.debug("L'utilisateur {} participe déjà à l'activité {}", user.getId(), ressource.getId());
        }
    }

    /**
     * Désinscrit un utilisateur d'une activité.
     *
     * @param ressourceId ID de la ressource (activité)
     * @param userId      ID de l'utilisateur qui quitte l'activité
     */
    @Transactional
    public void leaveActivity(Integer ressourceId, Integer userId) {
        activityParticipantRepository.findByRessource_IdAndUser_Id(ressourceId, userId)
                .ifPresentOrElse(
                        participation -> {
                            activityParticipantRepository.delete(participation);
                            logger.info("L'utilisateur {} a quitté l'activité {}", userId, ressourceId);
                        },
                        () -> logger.debug("L'utilisateur {} ne participe pas à l'activité {}", userId, ressourceId)
                );
    }

    /**
     * Recherche avancée pour les administrateurs.
     *
     * @param title      titre
     * @param categoryId catégorie
     * @param relationId relation
     * @param typeId     type
     * @param visibility visibilité
     * @param status     statut
     * @return liste des ressources
     */
    public List<Ressource> searchAdmin(String title, Integer categoryId, Integer relationId, Integer typeId,
            Visibility visibility, RessourceStatus status) {
        logger.debug("Recherche admin: title={}, categoryId={}, relationId={}, typeId={}, visibility={}, status={}", 
            title, categoryId, relationId, typeId, visibility, status);
        
        if (title != null && title.trim().isEmpty()) {
            title = null;
        }
        
        List<Ressource> results = ressourceRepository.searchAdmin(title, categoryId, relationId, typeId, visibility, status);
        logger.info("Recherche admin trouvée: {} ressources", results.size());
        return results;
    }

    /**
     * Récupère les ressources en attente de modération.
     *
     * @return liste des ressources pending
     */
    public List<Ressource> getPendingRessources() {
        logger.debug("Récupération des ressources en attente de modération");
        return ressourceRepository.findByStatusOrderByCreatedAtDesc(RessourceStatus.pending);
    }

    /**
     * Vérifie si un utilisateur peut voir une ressource.
     * Une ressource est visible si elle est publiée, ou si l'utilisateur en est l'auteur ou modérateur.
     *
     * @param userId ID de l'utilisateur
     * @param ressourceId ID de la ressource
     * @return true si l'utilisateur peut voir la ressource
     */
    public boolean canViewRessource(Integer userId, Integer ressourceId) {
        logger.debug("Vérification de la visibilité de la ressource {} pour l'utilisateur {}", 
            ressourceId, userId);
        
        Optional<Ressource> ressourceOpt = getById(ressourceId);
        if (!ressourceOpt.isPresent()) {
            logger.warn("Ressource {} non trouvée", ressourceId);
            return false;
        }

        Ressource ressource = ressourceOpt.get();
        
        // L'auteur peut toujours voir sa propre ressource
        if (userId != null && ressource.getUser().getId().equals(userId)) {
            logger.debug("Utilisateur {} est l'auteur de la ressource {}", userId, ressourceId);
            return true;
        }

        // Un modérateur ou supérieur peut toujours voir les ressources (ex: modération)
        if (userId != null && userService.isModerator(userId)) {
            logger.debug("Utilisateur {} est modérateur ou supérieur et peut voir la ressource {}", userId, ressourceId);
            return true;
        }

        // Si la ressource n'est pas publiée, elle n'est visible que pour l'auteur ou un modérateur
        if (ressource.getStatus() != RessourceStatus.published) {
            logger.debug("Ressource {} n'est pas publiée (statut: {}) - Accès refusé", ressourceId, ressource.getStatus());
            return false;
        }

        // Vérification selon la visibilité
        Visibility visibility = ressource.getVisibility();
        if (visibility == Visibility.public_visibility) {
            logger.debug("Ressource {} est publique", ressourceId);
            return true;
        } else if (visibility == Visibility.shared) {
            if (userId == null) {
                logger.debug("Ressource {} est partagée mais l'utilisateur n'est pas authentifié", ressourceId);
                return false;
            }
            // L'utilisateur doit être ami avec l'auteur
            boolean areFriends = friendService.areFriends(userId, ressource.getUser().getId());
            logger.debug("Ressource {} est partagée. Utilisateur {} et Auteur {} sont amis: {}", 
                ressourceId, userId, ressource.getUser().getId(), areFriends);
            return areFriends;
        } else if (visibility == Visibility.private_visibility) {
            // Déjà géré par la vérification d'auteur ci-dessus
            logger.debug("Ressource {} est privée. Accès refusé pour l'utilisateur {}", ressourceId, userId);
            return false;
        }

        return false;
    }

    /**
     * Vérifie si un utilisateur est propriétaire ou modérateur d'une ressource.
     *
     * @param userId ID de l'utilisateur
     * @param ressourceId ID de la ressource
     * @return true si l'utilisateur est propriétaire ou modérateur
     */
    public boolean isOwnerOrModerator(Integer userId, Integer ressourceId) {
        logger.debug("Vérification propriétaire/modérateur de la ressource {} pour l'utilisateur {}", 
            ressourceId, userId);
        
        if (userId == null || ressourceId == null) {
            return false;
        }

        Optional<Ressource> ressourceOpt = getById(ressourceId);
        if (!ressourceOpt.isPresent()) {
            logger.warn("Ressource {} non trouvée", ressourceId);
            return false;
        }

        Ressource ressource = ressourceOpt.get();
        
        // Propriétaire
        if (ressource.getUser().getId().equals(userId)) {
            logger.debug("Utilisateur {} est propriétaire de la ressource {}", userId, ressourceId);
            return true;
        }

        // Modérateur
        boolean isMod = userService.isModerator(userId);
        if (isMod) {
            logger.debug("Utilisateur {} est modérateur ou supérieur, autorisé pour la ressource {}", userId, ressourceId);
        }
        return isMod;
    }

    @Transactional
    public void updateStatus(Integer id, RessourceStatus status) {
        logger.debug("Mise à jour du statut de la ressource {} vers {}", id, status);
        Optional<Ressource> opt = ressourceRepository.findById(id);
        if (opt.isPresent()) {
            Ressource r = opt.get();
            r.setStatus(status);
            ressourceRepository.save(r);
        }
    }
}