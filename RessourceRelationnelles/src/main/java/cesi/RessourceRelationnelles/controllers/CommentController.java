package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.dtos.CommentDTO;
import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.services.CommentService;
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
 * Contrôleur REST API pour la gestion des commentaires.
 * Utilise CommentDTO pour le transfert de données et la validation.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAll() {
        logger.info("REST request to get all Comments");
        List<CommentDTO> comments = commentService.getAll().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getOne(@PathVariable Integer id) {
        logger.info("REST request to get Comment : {}", id);
        return commentService.getById(id)
                .map(comment -> ResponseEntity.ok(DtoMapper.toDTO(comment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ressource/{ressourceId}")
    public ResponseEntity<List<CommentDTO>> getByRessource(@PathVariable Integer ressourceId) {
        logger.info("REST request to get Comments by Ressource ID: {}", ressourceId);
        List<CommentDTO> comments = commentService.getByRessource(ressourceId).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getByUser(@PathVariable Integer userId) {
        logger.info("REST request to get Comments by User ID: {}", userId);
        List<CommentDTO> comments = commentService.getByUser(userId).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<CommentDTO>> getByParent(@PathVariable Integer parentId) {
        logger.info("REST request to get Comments by Parent ID: {}", parentId);
        List<CommentDTO> comments = commentService.getByParent(parentId).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentDTO commentDTO) {
        logger.info("REST request to create Comment");
        Comment comment = DtoMapper.toEntity(commentDTO);
        comment.setId(null);
        Comment saved = commentService.save(comment);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Integer id, @Valid @RequestBody CommentDTO commentDTO) {
        logger.info("REST request to update Comment : {}", id);
        if (commentService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Comment comment = DtoMapper.toEntity(commentDTO);
        comment.setId(id);
        Comment saved = commentService.save(comment);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("REST request to delete Comment : {}", id);
        if (commentService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}