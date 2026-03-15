package cesi.RessourceRelationnelles.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ressource_id", nullable = false)
    private Ressource ressource;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    public Comment() {
    }

    public Comment(Integer id, User user, String content, LocalDateTime createdAt, Ressource ressource,
            Comment parent) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.ressource = ressource;
        this.parent = parent;
    }
}
