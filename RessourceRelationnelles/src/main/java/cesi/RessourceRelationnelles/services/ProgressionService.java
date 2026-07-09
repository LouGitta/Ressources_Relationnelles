package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ProgressionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la progression des utilisateurs.
 * Gère les favoris et la progression de lecture des ressources.
 */
@Service
public class ProgressionService {

    private static final Logger logger = LoggerFactory.getLogger(ProgressionService.class);

    @Autowired
    private ProgressionRepository progressionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RessourceService ressourceService;

    /**
     * Récupère la progression d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return liste des progressions
     */
    public List<Progression> getByUser(Integer userId) {
        logger.debug("Récupération de la progression de l'utilisateur {}", userId);
        return progressionRepository.findByUser_Id(userId);
    }

    /**
     * Récupère la progression d'une ressource.
     *
     * @param ressourceId ID de la ressource
     * @return liste des progressions
     */
    public List<Progression> getByRessource(Integer ressourceId) {
        logger.debug("Récupération de la progression pour la ressource {}", ressourceId);
        return progressionRepository.findByRessource_Id(ressourceId);
    }

    /**
     * Sauvegarde une progression.
     *
     * @param progression la progression à sauvegarder
     * @return la progression sauvegardée
     */
    @Transactional
    public Progression save(Progression progression) {
        logger.debug("Sauvegarde de la progression pour l'utilisateur {} et ressource {}", 
            progression.getUser().getId(), progression.getRessource().getId());
        return progressionRepository.save(progression);
    }

    /**
     * Récupère les favoris d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return liste des progressions favorites
     */
    public List<Progression> getFavoritesByUser(Integer userId) {
        logger.debug("Récupération des favoris de l'utilisateur {}", userId);
        return progressionRepository.findByUser_IdAndIsFavoriteTrue(userId);
    }

    /**
     * Récupère la progression d'un utilisateur pour une ressource spécifique.
     *
     * @param userId      ID de l'utilisateur
     * @param ressourceId ID de la ressource
     * @return Optional contenant la progression ou vide
     */
    public Optional<Progression> getByUserAndRessource(Integer userId, Integer ressourceId) {
        logger.debug("Récupération de la progression pour l'utilisateur {} et ressource {}", userId, ressourceId);
        return progressionRepository.findByUser_IdAndRessource_Id(userId, ressourceId);
    }

    /**
     * Bascule le statut favori d'une ressource pour un utilisateur.
     * Crée ou met à jour la progression.
     *
     * @param userId ID de l'utilisateur
     * @param ressourceId ID de la ressource
     */
    @Transactional
    public void toggleFavorite(Integer userId, Integer ressourceId) {
        logger.debug("Basculement du favori pour l'utilisateur {} et ressource {}", userId, ressourceId);
        
        Optional<Progression> progOpt = getByUserAndRessource(userId, ressourceId);

        if (progOpt.isPresent()) {
            // La progression existe : inverser le statut favorite
            Progression progression = progOpt.get();
            boolean newStatus = !progression.isFavorite();
            progression.setFavorite(newStatus);
            save(progression);
            logger.info("Favori de l'utilisateur {} pour la ressource {} basculé à {}", 
                userId, ressourceId, newStatus);
        } else {
            // La progression n'existe pas : en créer une nouvelle avec favorite=true
            User user = userService.getById(userId).orElseThrow();
            Ressource ressource = ressourceService.getById(ressourceId).orElseThrow();

            Progression newProgression = new Progression();
            newProgression.setUser(user);
            newProgression.setRessource(ressource);
            newProgression.setFavorite(true);
            save(newProgression);
            logger.info("Nouvelle progression créée (favori) pour l'utilisateur {} et ressource {}", 
                userId, ressourceId);
        }
    }

    public long countAll() {
        return progressionRepository.count();
    }

}