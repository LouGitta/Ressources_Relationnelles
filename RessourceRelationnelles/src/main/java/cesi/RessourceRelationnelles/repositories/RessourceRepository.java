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
  
       List<Ressource> findByStatusOrderByCreatedAtDesc(RessourceStatus status);

       @Query("SELECT r FROM Ressource r WHERE " +
                     "(:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
                     "(:categoryId IS NULL OR r.category.id = :categoryId) AND " +
                     "(:visibility IS NULL OR r.visibility = :visibility) AND " +
                     "(:status IS NULL OR r.status = :status) " +
                     "ORDER BY r.createdAt DESC")
       List<Ressource> findWithFilters(@Param("title") String title,
                     @Param("categoryId") Integer categoryId,
                     @Param("visibility") Visibility visibility,
                     @Param("status") RessourceStatus status);

       @Query("SELECT new cesi.RessourceRelationnelles.dtos.StatItemDTO(r.category.name, COUNT(r)) FROM Ressource r GROUP BY r.category.name")
       List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countRessourcesByCategory();

       @Query("SELECT new cesi.RessourceRelationnelles.dtos.StatItemDTO(r.status, COUNT(r)) FROM Ressource r GROUP BY r.status")
       List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countRessourcesByStatus();

       @Query("SELECT new cesi.RessourceRelationnelles.dtos.StatItemDTO(r.visibility, COUNT(r)) FROM Ressource r GROUP BY r.visibility")
       List<cesi.RessourceRelationnelles.dtos.StatItemDTO> countRessourcesByVisibility();

       @Query("SELECT r FROM Ressource r WHERE " +
                     "(:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
                     "(:categoryId IS NULL OR r.category.id = :categoryId) AND " +
                     "(:relationId IS NULL OR r.relation.id = :relationId) AND " + // NOUVEAU
                     "(:typeId IS NULL OR r.type.id = :typeId) AND " + // NOUVEAU
                     "(:visibility IS NULL OR r.visibility = :visibility) AND " +
                     "(:status IS NULL OR r.status = :status) " +
                     "ORDER BY r.createdAt DESC")
       List<Ressource> searchAdmin(@Param("title") String title,
                     @Param("categoryId") Integer categoryId,
                     @Param("relationId") Integer relationId,
                     @Param("typeId") Integer typeId,
                     @Param("visibility") Visibility visibility,
                     @Param("status") RessourceStatus status);
}