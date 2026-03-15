package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Ressource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RessourceRepository extends CrudRepository<Ressource, Integer> {

    @Override
    List<Ressource> findAll();
}