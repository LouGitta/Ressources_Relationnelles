package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.repositories.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les types de ressources.
 * Centralise la logique métier liée aux types avec logging.
 */
@Service
public class TypeService {

    private static final Logger logger = LoggerFactory.getLogger(TypeService.class);

    @Autowired
    private TypeRepository typeRepository;

    /**
     * Récupère tous les types.
     *
     * @return liste de tous les types
     */
    public List<Type> getAll() {
        logger.debug("Récupération de tous les types");
        return typeRepository.findAll();
    }

    /**
     * Récupère un type par son ID.
     *
     * @param id ID du type
     * @return Optional contenant le type ou vide
     */
    public Optional<Type> getById(Integer id) {
        logger.debug("Récupération du type {}", id);
        return typeRepository.findById(id);
    }

    /**
     * Sauvegarde un type.
     *
     * @param type le type à sauvegarder
     * @return le type sauvegardé
     */
    @Transactional
    public Type save(Type type) {
        logger.info("Sauvegarde du type: {}", type.getName());
        return typeRepository.save(type);
    }

    /**
     * Supprime un type.
     *
     * @param id ID du type à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.warn("Suppression du type {}", id);
        typeRepository.deleteById(id);
    }

    /**
     * Compte le nombre total de types.
     *
     * @return nombre de types
     */
    public long countAll() {
        logger.debug("Comptage total des types");
        return typeRepository.count();
    }
}