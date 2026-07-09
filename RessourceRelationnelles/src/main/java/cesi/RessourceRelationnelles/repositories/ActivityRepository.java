package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Integer> {
    List<Activity> findAllByOrderByEventDateAsc();
    
    List<Activity> findByCreator_Id(Integer creatorId);
}