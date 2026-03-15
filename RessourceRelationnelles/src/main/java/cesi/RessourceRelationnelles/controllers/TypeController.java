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

import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.services.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping
    public ResponseEntity<List<Type>> getAll() {
        List<Type> categories = typeService.getAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Type> getOne(@PathVariable Integer id) {
        return typeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Type> create(@RequestBody Type type) {
        type.setId(null);
        Type savedType = typeService.save(type);
        return ResponseEntity.ok(savedType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Type> save(@PathVariable Integer id, @RequestBody Type type) {
        type.setId(id);
        Type savedType = typeService.save(type);
        return ResponseEntity.ok(savedType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        typeService.delete(id);
        return ResponseEntity.noContent().build(); // Renvoie 204 No Content
    }

}
