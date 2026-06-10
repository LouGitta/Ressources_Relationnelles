package cesi.RessourceRelationnelles.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import cesi.RessourceRelationnelles.config.AppConstants;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Comment.
 */
public class CommentDTO {

    private Integer id;

    @NotBlank(message = "Le contenu du commentaire est requis")
    @Size(min = 1, max = AppConstants.CONTENT_MAX_LENGTH,
            message = "Le commentaire doit avoir moins de " + AppConstants.CONTENT_MAX_LENGTH + " caractères")
    private String content;

    @NotNull(message = "L'ID de l'utilisateur est requis")
    private Integer userId;

    @NotNull(message = "L'ID de la ressource est requis")
    private Integer ressourceId;

    private Integer parentId; // Pour les réponses à des commentaires

    private LocalDateTime createdAt;

    // ==================== CONSTRUCTEURS ====================

    public CommentDTO() {
    }

    public CommentDTO(Integer id, String content, Integer userId, Integer ressourceId, 
                      Integer parentId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.ressourceId = ressourceId;
        this.parentId = parentId;
        this.createdAt = createdAt;
    }

    // ==================== GETTERS & SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
