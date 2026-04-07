package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Activity;
import cesi.RessourceRelationnelles.models.ActivityParticipant;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.ActivityParticipantRepository;
import cesi.RessourceRelationnelles.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired private ActivityRepository activityRepository;
    @Autowired private ActivityParticipantRepository participantRepository;

    // --- GESTION DES ACTIVITÉS ---
    public List<Activity> getAllActivities() {
        return activityRepository.findAllByOrderByEventDateAsc();
    }

    public Optional<Activity> getActivityById(Integer id) {
        return activityRepository.findById(id);
    }

    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteActivity(Integer id) {
        activityRepository.deleteById(id);
    }

    // --- GESTION DES PARTICIPANTS ---
    
    // Rejoindre une activité
    public void joinActivity(Activity activity, User user) {
        // On vérifie qu'il n'est pas déjà inscrit
        if (participantRepository.findByActivity_IdAndUser_Id(activity.getId(), user.getId()).isEmpty()) {
            ActivityParticipant participation = new ActivityParticipant();
            participation.setActivity(activity);
            participation.setUser(user);
            participation.setJoinedAt(LocalDateTime.now());
            participantRepository.save(participation);
        }
    }

    // Quitter une activité
    public void leaveActivity(Integer activityId, Integer userId) {
        Optional<ActivityParticipant> participation = participantRepository.findByActivity_IdAndUser_Id(activityId, userId);
        participation.ifPresent(p -> participantRepository.delete(p));
    }

    // Savoir si l'utilisateur participe à l'activité
    public boolean isUserParticipating(Integer activityId, Integer userId) {
        return participantRepository.findByActivity_IdAndUser_Id(activityId, userId).isPresent();
    }
    
    // Obtenir toutes les participations d'un utilisateur (pour son profil)
    public List<ActivityParticipant> getUserParticipations(Integer userId) {
        return participantRepository.findByUser_Id(userId);
    }
    
    // Obtenir la liste des participants pour une activité
    public List<ActivityParticipant> getParticipantsForActivity(Integer activityId) {
        return participantRepository.findByActivity_Id(activityId);
    }
}