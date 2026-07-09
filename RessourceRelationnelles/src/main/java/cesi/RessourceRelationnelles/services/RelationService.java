package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.repositories.RelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les relations entre ressources.
 * Centralise la logique métier liée aux relations avec logging.
 */
@Service
public class RelationService {

    private static final Logger logger = LoggerFactory.getLogger(RelationService.class);

    @Autowired
    private RelationRepository relationRepository;

    /**
     * Récupère toutes les relations.
     *
     * @return liste de toutes les relations
     */
    public List<Relation> getAll() {
        logger.debug("Récupération de toutes les relations");
        return relationRepository.findAll();
    }

    /**
     * Récupère une relation par son ID.
     *
     * @param id ID de la relation
     * @return Optional contenant la relation ou vide
     */
    public Optional<Relation> getById(Integer id) {
        logger.debug("Récupération de la relation {}", id);
        return relationRepository.findById(id);
    }

    /**
     * Sauvegarde une relation.
     *
     * @param relation la relation à sauvegarder
     * @return la relation sauvegardée
     */
    @Transactional
    public Relation save(Relation relation) {
        logger.info("Sauvegarde de la relation: {}", relation.getName());
        return relationRepository.save(relation);
    }

    /**
     * Supprime une relation.
     *
     * @param id ID de la relation à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.warn("Suppression de la relation {}", id);
        relationRepository.deleteById(id);
    }

    /**
     * Compte le nombre total de relations.
     *
     * @return nombre de relations
     */
    public long countAll() {
        logger.debug("Comptage total des relations");
        return relationRepository.count();
    }
}