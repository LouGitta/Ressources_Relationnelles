package cesi.RessourceRelationnelles.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertisseur JPA pour le type Role.
 * Rend la désérialisation de la base de données insensible à la casse et supporte
 * les différentes variantes d'écriture (ex: "moderator" -> MODERATOR, "super_admin" -> SUPERADMIN).
 */
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        String cleanData = dbData.trim().toUpperCase();
        try {
            return Role.valueOf(cleanData);
        } catch (IllegalArgumentException e) {
            // Gérer les variantes d'écriture
            String normalized = cleanData.replace("_", "").replace(" ", "");
            try {
                return Role.valueOf(normalized);
            } catch (IllegalArgumentException ex) {
                // Fallback de sécurité par défaut
                return Role.CITIZEN;
            }
        }
    }
}
