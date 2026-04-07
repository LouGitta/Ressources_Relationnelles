package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.repositories.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    public List<Relation> getAll() {
        return relationRepository.findAll();
    }

    public Optional<Relation> getById(Integer id) {
        return relationRepository.findById(id);
    }

    public Relation save(Relation relation) {
        return relationRepository.save(relation);
    }

    public void delete(Integer id) {
        relationRepository.deleteById(id);
    }

    public long countAll() {
        return relationRepository.count();
    }
}