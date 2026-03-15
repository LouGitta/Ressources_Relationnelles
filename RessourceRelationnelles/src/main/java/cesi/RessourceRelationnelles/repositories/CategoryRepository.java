package cesi.RessourceRelationnelles.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesi.RessourceRelationnelles.models.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
	@Override
    List<Category> findAll();
	
}
