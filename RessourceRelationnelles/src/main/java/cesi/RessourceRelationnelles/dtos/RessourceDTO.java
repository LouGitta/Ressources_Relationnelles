package cesi.RessourceRelationnelles.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Ressource.
 */
public class RessourceDTO {

    private Integer id;

    @NotBlank(message = "Le titre est requis")
    @Size(min = AppConstants.TITLE_MIN_LENGTH, max = AppConstants.TITLE_MAX_LENGTH,
            message = "Le titre doit avoir entre " + AppConstants.TITLE_MIN_LENGTH + 
                    " et " + AppConstants.TITLE_MAX_LENGTH + " caractères")
    private String title;

    @NotBlank(message = "Le contenu est requis")
    @Size(min = AppConstants.CONTENT_MIN_LENGTH, max = AppConstants.CONTENT_MAX_LENGTH,
            message = "Le contenu doit avoir entre " + AppConstants.CONTENT_MIN_LENGTH + 
                    " et " + AppConstants.CONTENT_MAX_LENGTH + " caractères")
    private String content;

    private Integer views;

    @NotNull(message = "L'ID de l'utilisateur est requis")
    private Integer userId;

    @NotNull(message = "L'ID de la relation est requis")
    private Integer relationId;

    @NotNull(message = "L'ID du type est requis")
    private Integer typeId;

    @NotNull(message = "L'ID de la catégorie est requis")
    private Integer categoryId;

    private Visibility visibility;

    private RessourceStatus status;

    private LocalDateTime createdAt;

    // ==================== CONSTRUCTEURS ====================

    public RessourceDTO() {
    }

    public RessourceDTO(Integer id, String title, String content, Integer views, Integer userId, 
                        Integer relationId, Integer typeId, Integer categoryId, 
                        Visibility visibility, RessourceStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.userId = userId;
        this.relationId = relationId;
        this.typeId = typeId;
        this.categoryId = categoryId;
        this.visibility = visibility;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ==================== GETTERS & SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public RessourceStatus getStatus() {
        return status;
    }

    public void setStatus(RessourceStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
