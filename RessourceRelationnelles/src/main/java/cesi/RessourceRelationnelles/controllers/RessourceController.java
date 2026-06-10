package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.dtos.RessourceDTO;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.services.RessourceService;
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
 * Contrôleur REST API pour la gestion des ressources.
 * Utilise RessourceDTO pour le typage des transferts de données.
 */
@RestController
@RequestMapping("/api/ressources")
public class RessourceController {

    private static final Logger logger = LoggerFactory.getLogger(RessourceController.class);

    @Autowired
    private RessourceService ressourceService;

    @GetMapping
    public ResponseEntity<List<RessourceDTO>> getAll() {
        logger.info("REST request to get all Ressources");
        List<RessourceDTO> resources = ressourceService.getAll().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RessourceDTO> getOne(@PathVariable Integer id) {
        logger.info("REST request to get Ressource : {}", id);
        return ressourceService.getById(id)
                .map(res -> ResponseEntity.ok(DtoMapper.toDTO(res)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RessourceDTO> create(@Valid @RequestBody RessourceDTO ressourceDTO) {
        logger.info("REST request to create Ressource : '{}'", ressourceDTO.getTitle());
        Ressource ressource = DtoMapper.toEntity(ressourceDTO);
        ressource.setId(null);
        Ressource saved = ressourceService.save(ressource);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RessourceDTO> update(@PathVariable Integer id, @Valid @RequestBody RessourceDTO ressourceDTO) {
        logger.info("REST request to update Ressource : {}", id);
        if (ressourceService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ressource ressource = DtoMapper.toEntity(ressourceDTO);
        ressource.setId(id);
        Ressource saved = ressourceService.save(ressource);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("REST request to delete Ressource : {}", id);
        if (ressourceService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ressourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}