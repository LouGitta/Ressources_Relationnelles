package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.dtos.ProgressionDTO;
import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.services.ProgressionService;
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
 * Contrôleur REST API pour la gestion des progressions et favoris des utilisateurs.
 * Utilise ProgressionDTO pour le typage et les validations.
 */
@RestController
@RequestMapping("/api/progressions")
public class ProgressionController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressionController.class);

    @Autowired
    private ProgressionService progressionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProgressionDTO>> getByUser(@PathVariable Integer userId) {
        logger.info("REST request to get Progressions for User: {}", userId);
        List<ProgressionDTO> list = progressionService.getByUser(userId).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/ressource/{ressourceId}")
    public ResponseEntity<List<ProgressionDTO>> getByRessource(@PathVariable Integer ressourceId) {
        logger.info("REST request to get Progressions for Ressource: {}", ressourceId);
        List<ProgressionDTO> list = progressionService.getByRessource(ressourceId).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ProgressionDTO> save(@Valid @RequestBody ProgressionDTO progressionDTO) {
        logger.info("REST request to save Progression");
        Progression progression = DtoMapper.toEntity(progressionDTO);
        Progression saved = progressionService.save(progression);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }
}