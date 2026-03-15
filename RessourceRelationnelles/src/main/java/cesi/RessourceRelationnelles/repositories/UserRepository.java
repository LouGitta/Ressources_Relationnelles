package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Override
    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}