package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.ActivityParticipant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityParticipantRepository extends CrudRepository<ActivityParticipant, Integer> {
    
    Optional<ActivityParticipant> findByRessource_IdAndUser_Id(Integer ressourceId, Integer userId);
    
    List<ActivityParticipant> findByRessource_Id(Integer ressourceId);
}