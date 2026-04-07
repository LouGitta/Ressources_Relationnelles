package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FriendRepository extends CrudRepository<Friend, Integer> {

    List<Friend> findByUser1_Id(Integer user1Id, FriendStatus status);

    List<Friend> findByUser2_Id(Integer user2Id, FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE f.user1.id = :uId")
    List<Friend> findAllByUser1_Id(@Param("uId") Integer uId);
    
    @Query("SELECT f FROM Friend f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = :status")
    List<Friend> findAllAcceptedByUserId(@Param("userId") Integer userId, @Param("status") FriendStatus status);
}