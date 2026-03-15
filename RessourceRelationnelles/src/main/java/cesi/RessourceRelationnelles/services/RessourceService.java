package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Ressource;
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
}