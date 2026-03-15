package cesi.RessourceRelationnelles.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Ressource")
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer views = 0;

    @ManyToOne
    @JoinColumn(name = "relation_id", nullable = false)
    private Relation relation;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.private_visibility;

    @Enumerated(EnumType.STRING)
    private RessourceStatus status = RessourceStatus.pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Ressource() {
    }

    public Ressource(Integer id, String title, String content, Integer views, Relation relation, Type type,
            Category category, Visibility visibility, RessourceStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.relation = relation;
        this.type = type;
        this.category = category;
        this.visibility = visibility;
        this.status = status;
        this.createdAt = createdAt;
    }

}
