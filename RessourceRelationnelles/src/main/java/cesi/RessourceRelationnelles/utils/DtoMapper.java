package cesi.RessourceRelationnelles.utils;

import cesi.RessourceRelationnelles.dtos.*;
import cesi.RessourceRelationnelles.models.*;

/**
 * Convertisseur utilitaire (Mapper) pour transformer les entités JPA en DTOs et vice-versa.
 * Centralise le mapping de données pour éviter les duplications et respecter SRP.
 */
public final class DtoMapper {

    private DtoMapper() {
        // Classe utilitaire non instantiable
    }

    // ==================== USER ====================
    
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.isActive()
        );
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setCreatedAt(dto.getCreatedAt());
        user.setActive(dto.isActive());
        return user;
    }

    // ==================== RESSOURCE ====================
    
    public static RessourceDTO toDTO(Ressource ressource) {
        if (ressource == null) {
            return null;
        }
        return new RessourceDTO(
            ressource.getId(),
            ressource.getTitle(),
            ressource.getContent(),
            ressource.getViews(),
            ressource.getUser() != null ? ressource.getUser().getId() : null,
            ressource.getRelation() != null ? ressource.getRelation().getId() : null,
            ressource.getType() != null ? ressource.getType().getId() : null,
            ressource.getCategory() != null ? ressource.getCategory().getId() : null,
            ressource.getVisibility(),
            ressource.getStatus(),
            ressource.getCreatedAt()
        );
    }

    public static Ressource toEntity(RessourceDTO dto) {
        if (dto == null) {
            return null;
        }
        Ressource ressource = new Ressource();
        ressource.setId(dto.getId());
        ressource.setTitle(dto.getTitle());
        ressource.setContent(dto.getContent());
        ressource.setViews(dto.getViews() != null ? dto.getViews() : 0);
        ressource.setVisibility(dto.getVisibility());
        ressource.setStatus(dto.getStatus());
        ressource.setCreatedAt(dto.getCreatedAt());

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            ressource.setUser(u);
        }
        if (dto.getRelationId() != null) {
            Relation r = new Relation();
            r.setId(dto.getRelationId());
            ressource.setRelation(r);
        }
        if (dto.getTypeId() != null) {
            Type t = new Type();
            t.setId(dto.getTypeId());
            ressource.setType(t);
        }
        if (dto.getCategoryId() != null) {
            Category c = new Category();
            c.setId(dto.getCategoryId());
            ressource.setCategory(c);
        }
        return ressource;
    }

    // ==================== COMMENT ====================
    
    public static CommentDTO toDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getUser() != null ? comment.getUser().getId() : null,
            comment.getRessource() != null ? comment.getRessource().getId() : null,
            comment.getParent() != null ? comment.getParent().getId() : null,
            comment.getCreatedAt()
        );
    }

    public static Comment toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        comment.setCreatedAt(dto.getCreatedAt());

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            comment.setUser(u);
        }
        if (dto.getRessourceId() != null) {
            Ressource r = new Ressource();
            r.setId(dto.getRessourceId());
            comment.setRessource(r);
        }
        if (dto.getParentId() != null) {
            Comment parent = new Comment();
            parent.setId(dto.getParentId());
            comment.setParent(parent);
        }
        return comment;
    }

    // ==================== FRIEND ====================
    
    public static FriendDTO toDTO(Friend friend) {
        if (friend == null) {
            return null;
        }
        return new FriendDTO(
            friend.getId(),
            friend.getUser1() != null ? friend.getUser1().getId() : null,
            friend.getUser2() != null ? friend.getUser2().getId() : null,
            friend.getStatus(),
            friend.getCreatedAt()
        );
    }

    public static Friend toEntity(FriendDTO dto) {
        if (dto == null) {
            return null;
        }
        Friend friend = new Friend();
        friend.setId(dto.getId());
        friend.setStatus(dto.getStatus());
        friend.setCreatedAt(dto.getCreatedAt());

        if (dto.getUser1Id() != null) {
            User u1 = new User();
            u1.setId(dto.getUser1Id());
            friend.setUser1(u1);
        }
        if (dto.getUser2Id() != null) {
            User u2 = new User();
            u2.setId(dto.getUser2Id());
            friend.setUser2(u2);
        }
        return friend;
    }

    // ==================== PROGRESSION ====================
    
    public static ProgressionDTO toDTO(Progression progression) {
        if (progression == null) {
            return null;
        }
        return new ProgressionDTO(
            progression.getId(),
            progression.getUser() != null ? progression.getUser().getId() : null,
            progression.getRessource() != null ? progression.getRessource().getId() : null,
            progression.isFavorite(),
            progression.isViewed() ? 1 : 0,
            progression.isViewed() ? 100 : 0,
            progression.getUpdatedAt()
        );
    }

    public static Progression toEntity(ProgressionDTO dto) {
        if (dto == null) {
            return null;
        }
        Progression progression = new Progression();
        progression.setId(dto.getId());
        progression.setFavorite(dto.isFavorite());
        progression.setViewed(dto.getViewCount() != null && dto.getViewCount() > 0);
        progression.setSaved(false);
        progression.setUpdatedAt(dto.getLastViewedAt());

        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            progression.setUser(u);
        }
        if (dto.getRessourceId() != null) {
            Ressource r = new Ressource();
            r.setId(dto.getRessourceId());
            progression.setRessource(r);
        }
        return progression;
    }

    // ==================== ACTIVITY PARTICIPANT ====================
    
    public static ActivityParticipantDTO toDTO(ActivityParticipant participant) {
        if (participant == null) {
            return null;
        }
        return new ActivityParticipantDTO(
            participant.getId(),
            participant.getUser() != null ? participant.getUser().getId() : null,
            participant.getRessource() != null ? participant.getRessource().getId() : null,
            participant.getJoinedAt()
        );
    }

    public static ActivityParticipant toEntity(ActivityParticipantDTO dto) {
        if (dto == null) {
            return null;
        }
        ActivityParticipant participant = new ActivityParticipant();
        participant.setId(dto.getId());
        participant.setJoinedAt(dto.getJoinedAt());

        if (dto.getRessourceId() != null) {
            Ressource r = new Ressource();
            r.setId(dto.getRessourceId());
            participant.setRessource(r);
        }
        if (dto.getUserId() != null) {
            User u = new User();
            u.setId(dto.getUserId());
            participant.setUser(u);
        }
        return participant;
    }

    // ==================== RESSOURCE FORM ====================

    public static RessourceFormDTO toFormDTO(Ressource ressource) {
        if (ressource == null) {
            return null;
        }
        RessourceFormDTO dto = new RessourceFormDTO();
        dto.setId(ressource.getId());
        dto.setTitle(ressource.getTitle());
        dto.setContent(ressource.getContent());
        dto.setCategoryId(ressource.getCategory() != null ? ressource.getCategory().getId() : null);
        dto.setRelationId(ressource.getRelation() != null ? ressource.getRelation().getId() : null);
        dto.setTypeId(ressource.getType() != null ? ressource.getType().getId() : null);
        dto.setVisibility(ressource.getVisibility());
        dto.setStatus(ressource.getStatus());
        if (ressource.getUser() != null) {
            dto.setUser(new RessourceFormDTO.UserDtoForForm(ressource.getUser().getUsername()));
        }
        dto.setCreatedAt(ressource.getCreatedAt());
        dto.setViews(ressource.getViews());
        return dto;
    }

    public static Ressource toEntity(RessourceFormDTO dto) {
        if (dto == null) {
            return null;
        }
        Ressource ressource = new Ressource();
        ressource.setId(dto.getId());
        ressource.setTitle(dto.getTitle());
        ressource.setContent(dto.getContent());
        ressource.setVisibility(dto.getVisibility());
        ressource.setStatus(dto.getStatus());

        if (dto.getCategoryId() != null) {
            Category c = new Category();
            c.setId(dto.getCategoryId());
            ressource.setCategory(c);
        }
        if (dto.getRelationId() != null) {
            Relation r = new Relation();
            r.setId(dto.getRelationId());
            ressource.setRelation(r);
        }
        if (dto.getTypeId() != null) {
            Type t = new Type();
            t.setId(dto.getTypeId());
            ressource.setType(t);
        }
        return ressource;
    }

    // ==================== USER FORM ====================

    public static UserFormDTO toFormDTO(User user) {
        if (user == null) {
            return null;
        }
        UserFormDTO dto = new UserFormDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    public static User toEntity(UserFormDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
        return user;
    }
}
