package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.models.Progression;
import cesi.RessourceRelationnelles.services.ProgressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/progressions")
public class ProgressionController {

    @Autowired
    private ProgressionService progressionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Progression>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(progressionService.getByUser(userId));
    }

    @GetMapping("/ressource/{ressourceId}")
    public ResponseEntity<List<Progression>> getByRessource(@PathVariable Integer ressourceId) {
        return ResponseEntity.ok(progressionService.getByRessource(ressourceId));
    }

    @PostMapping
    public ResponseEntity<Progression> save(@RequestBody Progression progression) {
        return ResponseEntity.ok(progressionService.save(progression));
    }
}