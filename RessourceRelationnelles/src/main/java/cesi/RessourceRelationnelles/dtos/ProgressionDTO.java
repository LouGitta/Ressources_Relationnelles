package cesi.RessourceRelationnelles.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Progression (favoris et progression utilisateur).
 */
public class ProgressionDTO {

    private Integer id;

    @NotNull(message = "L'ID de l'utilisateur est requis")
    private Integer userId;

    @NotNull(message = "L'ID de la ressource est requis")
    private Integer ressourceId;

    private boolean isFavorite;

    private Integer viewCount;

    private Integer completionPercentage;

    private LocalDateTime lastViewedAt;

    // ==================== CONSTRUCTEURS ====================

    public ProgressionDTO() {
    }

    public ProgressionDTO(Integer id, Integer userId, Integer ressourceId, boolean isFavorite, 
                          Integer viewCount, Integer completionPercentage, LocalDateTime lastViewedAt) {
        this.id = id;
        this.userId = userId;
        this.ressourceId = ressourceId;
        this.isFavorite = isFavorite;
        this.viewCount = viewCount;
        this.completionPercentage = completionPercentage;
        this.lastViewedAt = lastViewedAt;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public LocalDateTime getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(LocalDateTime lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }
}
