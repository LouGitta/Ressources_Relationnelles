package cesi.RessourceRelationnelles.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Progression")
@Data
public class Progression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ressource_id", nullable = false)
    private Ressource ressource;

    @Column(name = "is_favorite")
    private boolean isFavorite = false;

    @Column(name = "is_saved")
    private boolean isSaved = false;

    @Column(name = "is_viewed")
    private boolean isViewed = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Progression() {
    }

    public Progression(Integer id, User user, Ressource ressource, boolean isFavorite, boolean isSaved,
            boolean isViewed, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.ressource = ressource;
        this.isFavorite = isFavorite;
        this.isSaved = isSaved;
        this.isViewed = isViewed;
        this.updatedAt = updatedAt;
    }

}