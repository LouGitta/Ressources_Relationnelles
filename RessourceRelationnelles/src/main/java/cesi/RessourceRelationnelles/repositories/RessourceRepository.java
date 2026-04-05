package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RessourceRepository extends CrudRepository<Ressource, Integer> {

    @Override
    List<Ressource> findAll();

    List<Ressource> findByUser_Id(Integer userId);

    List<Ressource> findTop10ByStatusOrderByCreatedAtDesc(RessourceStatus status);


       @Query("SELECT r.category.name, COUNT(r) FROM Ressource r GROUP BY r.category.name")
       List<Object[]> countRessourcesByCategory();

       @Query("SELECT r.status, COUNT(r) FROM Ressource r GROUP BY r.status")
       List<Object[]> countRessourcesByStatus();

       @Query("SELECT r.visibility, COUNT(r) FROM Ressource r GROUP BY r.visibility")
       List<Object[]> countRessourcesByVisibility();
}