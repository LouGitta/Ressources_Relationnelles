package cesi.RessourceRelationnelles.dtos;

import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;

/**
 * DTO générique pour encapsuler les résultats des agrégations statistiques (ex: count par catégorie).
 * Évite l'usage de Object[] non typé.
 */
public class StatItemDTO {
    private String key;
    private Long count;

    public StatItemDTO() {
    }

    public StatItemDTO(String key, Long count) {
        this.key = key;
        this.count = count;
    }

    public StatItemDTO(RessourceStatus status, Long count) {
        this.key = status != null ? status.name() : null;
        this.count = count;
    }

    public StatItemDTO(Visibility visibility, Long count) {
        this.key = visibility != null ? visibility.name() : null;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
