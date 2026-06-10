package cesi.RessourceRelationnelles.dtos;

import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour recevoir les données des formulaires de création/édition d'une Ressource.
 */
public class RessourceFormDTO {

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

    @NotNull(message = "La catégorie est requise")
    private Integer categoryId;

    @NotNull(message = "La relation est requise")
    private Integer relationId;

    @NotNull(message = "Le type est requis")
    private Integer typeId;

    private Visibility visibility;

    private RessourceStatus status;

    private UserDtoForForm user;
    private java.time.LocalDateTime createdAt;
    private Integer views;

    public static class UserDtoForForm {
        private String username;
        public UserDtoForForm(String username) { this.username = username; }
        public String getUsername() { return username; }
    }

    public UserDtoForForm getUser() {
        return user;
    }

    public void setUser(UserDtoForForm user) {
        this.user = user;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public RessourceFormDTO() {
    }

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
}
