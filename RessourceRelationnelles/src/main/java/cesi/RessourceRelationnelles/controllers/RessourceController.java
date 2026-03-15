package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.services.RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ressources")
public class RessourceController {

    @Autowired
    private RessourceService ressourceService;

    @GetMapping
    public ResponseEntity<List<Ressource>> getAll() {
        return ResponseEntity.ok(ressourceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ressource> getOne(@PathVariable Integer id) {
        return ressourceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ressource> create(@RequestBody Ressource ressource) {
        ressource.setId(null);
        return ResponseEntity.ok(ressourceService.save(ressource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ressource> update(@PathVariable Integer id, @RequestBody Ressource ressource) {
        ressource.setId(id);
        return ResponseEntity.ok(ressourceService.save(ressource));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ressourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}