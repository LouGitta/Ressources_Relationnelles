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
        List<Friend> friends1 = friendRepository.findByUser1_Id(userId, FriendStatus.accepted);
        List<Friend> friends2 = friendRepository.findByUser2_Id(userId, FriendStatus.accepted);
        friends1.addAll(friends2);
        return friends1;
    }

    public List<Friend> getByUser1(Integer user1) {
        return friendRepository.findByUser1_Id(user1, FriendStatus.pending);
    }

    public List<Friend> getByUser2(Integer user2) {
        return friendRepository.findByUser2_Id(user2, FriendStatus.pending);
    }

    public Friend save(Friend friend) {
        return friendRepository.save(friend);
    }

    public void delete(Integer id) {
        friendRepository.deleteById(id);
    }
}