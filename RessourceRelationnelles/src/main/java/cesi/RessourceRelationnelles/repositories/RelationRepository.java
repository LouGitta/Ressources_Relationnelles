package cesi.RessourceRelationnelles.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesi.RessourceRelationnelles.models.Relation;

@Repository
public interface RelationRepository extends CrudRepository<Relation, Integer> {
    @Override
    List<Relation> findAll();

}
