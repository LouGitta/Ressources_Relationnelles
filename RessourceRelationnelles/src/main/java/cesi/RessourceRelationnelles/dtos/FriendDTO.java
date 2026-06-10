package cesi.RessourceRelationnelles.dtos;

import jakarta.validation.constraints.NotNull;
import cesi.RessourceRelationnelles.models.FriendStatus;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Friend (relation d'amitié).
 */
public class FriendDTO {

    private Integer id;

    @NotNull(message = "L'ID de l'utilisateur 1 est requis")
    private Integer user1Id;

    @NotNull(message = "L'ID de l'utilisateur 2 est requis")
    private Integer user2Id;

    private FriendStatus status;

    private LocalDateTime createdAt;

    // ==================== CONSTRUCTEURS ====================

    public FriendDTO() {
    }

    public FriendDTO(Integer id, Integer user1Id, Integer user2Id, FriendStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
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

    public Integer getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Integer user1Id) {
        this.user1Id = user1Id;
    }

    public Integer getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Integer user2Id) {
        this.user2Id = user2Id;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
