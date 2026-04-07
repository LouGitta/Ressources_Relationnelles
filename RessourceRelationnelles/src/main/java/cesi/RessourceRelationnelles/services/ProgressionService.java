package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ProgressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressionService {

    @Autowired
    private ProgressionRepository progressionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RessourceService ressourceService;

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

    public void toggleFavorite(Integer userId, Integer ressourceId) {
        Optional<Progression> progOpt = getByUserAndRessource(userId, ressourceId);

        if (progOpt.isPresent()) {
            // La progression existe : inverser le statut favorite
            Progression progression = progOpt.get();
            progression.setFavorite(!progression.isFavorite());
            save(progression);
        } else {
            // La progression n'existe pas : en créer une nouvelle avec favorite=true
            User user = userService.getById(userId).orElseThrow();
            Ressource ressource = ressourceService.getById(ressourceId).orElseThrow();

            Progression newProgression = new Progression();
            newProgression.setUser(user);
            newProgression.setRessource(ressource);
            newProgression.setFavorite(true);
            save(newProgression);
        }
    }

    public long countAll() {
        return progressionRepository.count();
    }

}