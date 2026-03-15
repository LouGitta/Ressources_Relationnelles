package cesi.RessourceRelationnelles.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesi.RessourceRelationnelles.models.Type;

@Repository
public interface TypeRepository extends CrudRepository<Type, Integer> {
	@Override
    List<Type> findAll();
	
}
