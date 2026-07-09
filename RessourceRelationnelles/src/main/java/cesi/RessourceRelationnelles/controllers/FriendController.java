package cesi.RessourceRelationnelles.controllers;

import cesi.RessourceRelationnelles.dtos.FriendDTO;
import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.services.FriendService;
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
 * Contrôleur REST API pour la gestion des relations d'amitié.
 * Utilise FriendDTO pour le transfert de données et la validation.
 */
@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @GetMapping("/user/{id}")
    public ResponseEntity<List<FriendDTO>> getAllFriend(@PathVariable Integer id) {
        logger.info("REST request to get accepted Friends for User: {}", id);
        List<FriendDTO> friends = friendService.getAllAcceptedFriends(id).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/sender/{id}")
    public ResponseEntity<List<FriendDTO>> getSentRequests(@PathVariable Integer id) {
        logger.info("REST request to get sent friend requests by User: {}", id);
        List<FriendDTO> friends = friendService.getByUser1(id).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/receiver/{id}")
    public ResponseEntity<List<FriendDTO>> getReceivedRequests(@PathVariable Integer id) {
        logger.info("REST request to get received friend requests by User: {}", id);
        List<FriendDTO> friends = friendService.getByUser2(id).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/{id}")
    public ResponseEntity<List<FriendDTO>> getAllFriendAndRequests(@PathVariable Integer id) {
        logger.info("REST request to get all friend connections/requests for User: {}", id);
        List<FriendDTO> friends = friendService.getAllByUser1(id).stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(friends);
    }

    @PostMapping
    public ResponseEntity<FriendDTO> create(@Valid @RequestBody FriendDTO friendDTO) {
        logger.info("REST request to create Friend relationship");
        Friend friend = DtoMapper.toEntity(friendDTO);
        friend.setId(null);
        Friend saved = friendService.save(friend);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendDTO> update(@PathVariable Integer id, @Valid @RequestBody FriendDTO friendDTO) {
        logger.info("REST request to update Friend relationship : {}", id);
        if (friendService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Friend friend = DtoMapper.toEntity(friendDTO);
        friend.setId(id);
        Friend saved = friendService.save(friend);
        return ResponseEntity.ok(DtoMapper.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("REST request to delete Friend relationship : {}", id);
        if (friendService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        friendService.delete(id);
        return ResponseEntity.noContent().build();
    }
}