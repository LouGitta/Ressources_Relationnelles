package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.repositories.RessourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RessourceService {

    @Autowired
    private RessourceRepository ressourceRepository;

    public List<Ressource> getAll() {
        return ressourceRepository.findAll();
    }

    public Optional<Ressource> getById(Integer id) {
        return ressourceRepository.findById(id);
    }

    public Ressource save(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    public void delete(Integer id) {
        ressourceRepository.deleteById(id);
    }

    public List<Ressource> getRecentRessources() {
        return ressourceRepository.findTop10ByStatusOrderByCreatedAtDesc(RessourceStatus.published);
    }

    public List<Ressource> searchAndFilter(String title, Integer categoryId, Visibility visibility,
            RessourceStatus status) {
        if (title != null && title.trim().isEmpty()) {
            title = null;
        }
        return ressourceRepository.findWithFilters(title, categoryId, visibility, status);
    }

    public List<Ressource> getByUser(Integer userId) {
        return ressourceRepository.findByUser_Id(userId);
    }

    public long countAll() {
        return ressourceRepository.count();
    }

    public List<Object[]> countRessourcesByCategory() {
        return ressourceRepository.countRessourcesByCategory();
    }

    public List<Object[]> countByStatus() {
        return ressourceRepository.countRessourcesByStatus();
    }

    public List<Object[]> countByVisibility() {
        return ressourceRepository.countRessourcesByVisibility();
    }

    public List<Ressource> searchAdmin(String title, Integer categoryId, Integer relationId, Integer typeId,
            Visibility visibility, RessourceStatus status) {
        if (title != null && title.trim().isEmpty()) {
            title = null;
        }
        return ressourceRepository.searchAdmin(title, categoryId, relationId, typeId, visibility, status);
    }

    public List<Ressource> getPendingRessources() {
        return ressourceRepository.findByStatusOrderByCreatedAtDesc(RessourceStatus.pending);
    }

    public void updateStatus(Integer id, RessourceStatus status) {
        Optional<Ressource> opt = ressourceRepository.findById(id);
        if (opt.isPresent()) {
            Ressource r = opt.get();
            r.setStatus(status);
            ressourceRepository.save(r);
        }
    }
}