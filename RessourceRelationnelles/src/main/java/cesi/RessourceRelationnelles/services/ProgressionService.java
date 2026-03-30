package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.repositories.ProgressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressionService {

    @Autowired
    private ProgressionRepository progressionRepository;

    public List<Progression> getByUser(Integer userId) {
        return progressionRepository.findByUser_Id(userId);
    }

    public List<Progression> getByRessource(Integer ressourceId) {
        return progressionRepository.findByRessource_Id(ressourceId);
    }

    public Progression save(Progression progression) {
        return progressionRepository.save(progression);
    }

    public List<Progression> getFavoritesByUser(Integer userId) {
    return progressionRepository.findByUser_IdAndIsFavoriteTrue(userId);
    }

    public Optional<Progression> getByUserAndRessource(Integer userId, Integer ressourceId) {
        return progressionRepository.findByUser_IdAndRessource_Id(userId, ressourceId);
    }

}