package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Override
    List<Comment> findAll();

    List<Comment> findByRessourceId(Integer ressourceId);

    List<Comment> findByParentId(Integer parentId);

    List<Comment> findByUserId(Integer userId);
}
