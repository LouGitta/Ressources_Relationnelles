package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public List<Comment> getByRessource(Integer ressourceId) {
        return commentRepository.findByRessourceId(ressourceId);
    }

    public List<Comment> getByUser(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> getByParent(Integer parentId) {
        return commentRepository.findByParentId(parentId);
    }

    public Optional<Comment> getById(Integer id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }

    public long countAll() {
        return commentRepository.count();
    }
}