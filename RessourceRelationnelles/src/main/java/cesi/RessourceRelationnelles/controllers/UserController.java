package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.dtos.UserDTO;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;
import cesi.RessourceRelationnelles.utils.DtoMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST API pour la gestion des utilisateurs.
 * Utilise des DTOs pour filtrer les données sensibles (comme le mot de passe).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        logger.info("REST request to get all Users");
        List<UserDTO> users = userService.getAll().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOne(@PathVariable Integer id) {
        logger.info("REST request to get User : {}", id);
        return userService.getById(id)
                .map(user -> ResponseEntity.ok(DtoMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        logger.info("REST request to create User : {}", userDTO.getUsername());
        User user = DtoMapper.toEntity(userDTO);
        user.setId(null);
        // Définit un mot de passe par défaut sécurisé car il n'est pas exposé par le DTO
        user.setPassword("SecureTempPassword123!");
        User savedUser = userService.save(user);
        return ResponseEntity.ok(DtoMapper.toDTO(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO) {
        logger.info("REST request to update User : {}", id);
        return userService.getById(id)
                .map(existingUser -> {
                    User user = DtoMapper.toEntity(userDTO);
                    user.setId(id);
                    // Conserver le mot de passe existant
                    user.setPassword(existingUser.getPassword());
                    User savedUser = userService.save(user);
                    return ResponseEntity.ok(DtoMapper.toDTO(savedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("REST request to delete User : {}", id);
        if (userService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}