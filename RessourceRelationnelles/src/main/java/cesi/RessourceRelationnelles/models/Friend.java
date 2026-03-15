package cesi.RessourceRelationnelles.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "Friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_2", nullable = false)
    private User user2;

    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Friend() {
    }

    public Friend(Integer id, User user1, User user2, FriendStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.createdAt = createdAt;
    }
}