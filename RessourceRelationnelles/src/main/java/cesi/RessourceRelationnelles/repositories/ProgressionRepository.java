package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Progression;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressionRepository extends CrudRepository<Progression, Integer> {

    List<Progression> findByUser_Id(Integer userId);

    List<Progression> findByRessource_Id(Integer ressourceId);

    List<Progression> findByUser_IdAndIsFavoriteTrue(Integer userId);

    Optional<Progression> findByUser_IdAndRessource_Id(Integer userId, Integer ressourceId);
}