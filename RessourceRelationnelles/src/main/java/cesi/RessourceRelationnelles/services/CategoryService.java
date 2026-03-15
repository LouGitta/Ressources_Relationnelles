package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Category;
import cesi.RessourceRelationnelles.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> getById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}