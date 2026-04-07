package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Friend>> getAllFriend(@PathVariable Integer id) {
        return ResponseEntity.ok(friendService.getAllAcceptedFriends(id));
    }

    @GetMapping("/sender/{id}")
    public ResponseEntity<List<Friend>> getSentRequests(@PathVariable Integer id) {
        return ResponseEntity.ok(friendService.getByUser1(id));
    }

    @GetMapping("/receiver/{id}")
    public ResponseEntity<List<Friend>> getReceivedRequests(@PathVariable Integer id) {
        return ResponseEntity.ok(friendService.getByUser2(id));
    }

        @GetMapping("/friends/{id}")
    public ResponseEntity<List<Friend>> getAllFriendAndRequests(@PathVariable Integer id) {
        return ResponseEntity.ok( friendService.getAllByUser1(id));
    }
    @PostMapping
    public ResponseEntity<Friend> create(@RequestBody Friend friend) {
        friend.setId(null);
        return ResponseEntity.ok(friendService.save(friend));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Friend> update(@PathVariable Integer id, @RequestBody Friend friend) {
        if (friendService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        friend.setId(id);
        return ResponseEntity.ok(friendService.save(friend));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        friendService.delete(id);
        return ResponseEntity.noContent().build();
    }
}