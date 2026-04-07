package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;
import cesi.RessourceRelationnelles.repositories.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    public Optional<Friend> getById(Integer id) {
        return friendRepository.findById(id);
    }

    public List<Friend> getAllAcceptedFriends(Integer userId) {
        return friendRepository.findAllAcceptedByUserId(userId, FriendStatus.accepted);
    }

    public List<Friend> getByUser1(Integer user1) {
        return friendRepository.findByUser1_Id(user1, FriendStatus.pending);
    }

    public List<Friend> getByUser2(Integer user2) {
        return friendRepository.findByUser2_Id(user2, FriendStatus.pending);
    }

    public List<Friend> getAllByUser1(Integer user1) {
        return friendRepository.findAllByUser1_Id(user1);
    }

    public Friend save(Friend friend) {
        return friendRepository.save(friend);
    }

    public void delete(Integer id) {
        friendRepository.deleteById(id);
    }
}