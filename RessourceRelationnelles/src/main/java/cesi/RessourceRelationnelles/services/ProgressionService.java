package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.repositories.ProgressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
}