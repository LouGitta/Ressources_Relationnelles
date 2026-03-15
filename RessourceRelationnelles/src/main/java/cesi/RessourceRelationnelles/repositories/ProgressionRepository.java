package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Progression;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressionRepository extends CrudRepository<Progression, Integer> {

    List<Progression> findByUser_Id(Integer userId);

    List<Progression> findByRessource_Id(Integer ressourceId);
}