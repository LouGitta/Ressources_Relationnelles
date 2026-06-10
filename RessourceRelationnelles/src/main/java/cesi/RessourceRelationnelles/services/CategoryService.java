package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Category;
import cesi.RessourceRelationnelles.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les catégories de ressources.
 * Centralise la logique métier liée aux catégories avec logging.
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Récupère toutes les catégories.
     *
     * @return liste de toutes les catégories
     */
    public List<Category> getAll() {
        logger.debug("Récupération de toutes les catégories");
        return categoryRepository.findAll();
    }

    /**
     * Récupère une catégorie par son ID.
     *
     * @param id ID de la catégorie
     * @return Optional contenant la catégorie ou vide
     */
    public Optional<Category> getById(Integer id) {
        logger.debug("Récupération de la catégorie {}", id);
        return categoryRepository.findById(id);
    }

    /**
     * Sauvegarde une catégorie.
     *
     * @param category la catégorie à sauvegarder
     * @return la catégorie sauvegardée
     */
    @Transactional
    public Category save(Category category) {
        logger.info("Sauvegarde de la catégorie: {}", category.getName());
        return categoryRepository.save(category);
    }

    /**
     * Supprime une catégorie.
     *
     * @param id ID de la catégorie à supprimer
     */
    @Transactional
    public void delete(Integer id) {
        logger.warn("Suppression de la catégorie {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Compte le nombre total de catégories.
     *
     * @return nombre de catégories
     */
    public long countAll() {
        logger.debug("Comptage total des catégories");
        return categoryRepository.count();
    }
}