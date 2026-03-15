package cesi.RessourceRelationnelles.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.services.RelationService;

@RestController
@RequestMapping("/api/relations")
public class RelationController {
    @Autowired
    private RelationService relationService;

    @GetMapping
    public ResponseEntity<List<Relation>> getAll() {
        List<Relation> categories = relationService.getAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relation> getOne(@PathVariable Integer id) {
        return relationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Relation> create(@RequestBody Relation relation) {
        relation.setId(null);
        Relation savedRelation = relationService.save(relation);
        return ResponseEntity.ok(savedRelation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relation> save(@PathVariable Integer id, @RequestBody Relation relation) {
        relation.setId(id);
        Relation savedRelation = relationService.save(relation);
        return ResponseEntity.ok(savedRelation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        relationService.delete(id);
        return ResponseEntity.noContent().build(); // Renvoie 204 No Content
    }

}
