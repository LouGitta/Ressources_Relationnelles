package cesi.RessourceRelationnelles.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité ActivityParticipant (participation à une activité).
 */
public class ActivityParticipantDTO {

    private Integer id;

    @NotNull(message = "L'ID de l'utilisateur est requis")
    private Integer userId;

    @NotNull(message = "L'ID de la ressource (activité) est requis")
    private Integer ressourceId;

    private LocalDateTime joinedAt;

    // ==================== CONSTRUCTEURS ====================

    public ActivityParticipantDTO() {
    }

    public ActivityParticipantDTO(Integer id, Integer userId, Integer ressourceId, LocalDateTime joinedAt) {
        this.id = id;
        this.userId = userId;
        this.ressourceId = ressourceId;
        this.joinedAt = joinedAt;
    }

    // ==================== GETTERS & SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Integer ressourceId) {
        this.ressourceId = ressourceId;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
