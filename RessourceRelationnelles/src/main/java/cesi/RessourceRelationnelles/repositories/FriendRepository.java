package cesi.RessourceRelationnelles.repositories;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FriendRepository extends CrudRepository<Friend, Integer> {

    List<Friend> findByUser1_Id(Integer user1Id, FriendStatus status);

    List<Friend> findByUser2_Id(Integer user2Id, FriendStatus status);
}