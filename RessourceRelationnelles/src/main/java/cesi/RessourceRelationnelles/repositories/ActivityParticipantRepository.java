package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.ActivityParticipant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityParticipantRepository extends CrudRepository<ActivityParticipant, Integer> {
    
    List<ActivityParticipant> findByActivity_Id(Integer activityId);
    
    List<ActivityParticipant> findByUser_Id(Integer userId);
    
    Optional<ActivityParticipant> findByActivity_IdAndUser_Id(Integer activityId, Integer userId);
}