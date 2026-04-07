package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getOne(@PathVariable Integer id) {
        return commentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ressource/{ressourceId}")
    public ResponseEntity<List<Comment>> getByRessource(@PathVariable Integer ressourceId) {
        return ResponseEntity.ok(commentService.getByRessource(ressourceId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(commentService.getByUser(userId));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Comment>> getByParent(@PathVariable Integer parentId) {
        return ResponseEntity.ok(commentService.getByParent(parentId));
    }

    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment) {
        comment.setId(null);
        return ResponseEntity.ok(commentService.save(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@PathVariable Integer id, @RequestBody Comment comment) {
        if (commentService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        comment.setId(id);
        return ResponseEntity.ok(commentService.save(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}