package cesi.RessourceRelationnelles.dtos;

import cesi.RessourceRelationnelles.config.AppConstants;
import cesi.RessourceRelationnelles.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour recevoir les données des formulaires de création/édition d'un User.
 */
public class UserFormDTO {

    private Integer id;

    @NotBlank(message = "Le nom d'utilisateur est requis")
    @Size(min = AppConstants.USERNAME_MIN_LENGTH, max = AppConstants.USERNAME_MAX_LENGTH,
            message = "Le nom d'utilisateur doit avoir entre " + AppConstants.USERNAME_MIN_LENGTH + 
                    " et " + AppConstants.USERNAME_MAX_LENGTH + " caractères")
    private String username;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;

    private String password;

    private Role role;

    private boolean isActive = true;

    public UserFormDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
