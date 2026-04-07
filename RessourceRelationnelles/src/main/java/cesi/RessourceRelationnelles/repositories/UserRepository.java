package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

        @Override
        List<User> findAll();

        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);

        @Query("SELECT u FROM User u WHERE " +
                        "(:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND "
                        +
                        "(:role IS NULL OR u.role = :role) AND " +
                        "(:isActive IS NULL OR u.isActive = :isActive) " +
                        "ORDER BY u.id DESC")
        List<User> findWithFilters(@Param("keyword") String keyword,
                        @Param("role") Role role,
                        @Param("isActive") Boolean isActive);
}