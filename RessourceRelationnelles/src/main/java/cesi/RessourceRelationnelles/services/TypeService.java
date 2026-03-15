package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    public List<Type> getAll() {
        return typeRepository.findAll();
    }
    
    public Optional<Type> getById(Integer id) {
        return typeRepository.findById(id);
    }

    public Type save(Type type) {
        return typeRepository.save(type);
    }
    
    public void delete(Integer id) {
        typeRepository.deleteById(id);
    }
}