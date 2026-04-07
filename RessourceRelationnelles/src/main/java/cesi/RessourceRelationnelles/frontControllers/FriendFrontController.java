package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.FriendService;
import cesi.RessourceRelationnelles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class FriendFrontController {

    @Autowired private FriendService friendService;
    
    // On garde userService uniquement pour aller chercher l'utilisateur cible (celui qu'on veut ajouter)
    @Autowired private UserService userService; 

    // --- ENVOYER UNE DEMANDE D'AMI ---
    @PostMapping("/app/friends/add/{targetUserId}")
    public String sendFriendRequest(@PathVariable Integer targetUserId, 
                                    @RequestParam Integer ressourceId, 
                                    Model model) { // <-- On injecte le Model ici
        
        // On récupère l'utilisateur directement depuis le Model préparé par le GlobalControllerAdvice
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";
        
        Optional<User> targetOpt = userService.getById(targetUserId);

        if (targetOpt.isPresent() && !currentUser.getId().equals(targetUserId)) {
            Friend requestFriend = new Friend();
            requestFriend.setUser1(currentUser);
            requestFriend.setUser2(targetOpt.get());
            requestFriend.setStatus(FriendStatus.pending);
            requestFriend.setCreatedAt(LocalDateTime.now());
            
            friendService.save(requestFriend);
        }

        return "redirect:/app/ressources/" + ressourceId;
    }

    // --- ACCEPTER UNE DEMANDE ---
    @PostMapping("/app/friends/{id}/accept")
    public String acceptFriend(@PathVariable Integer id, Model model) {
        
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        Optional<Friend> friendOpt = friendService.getById(id);
        if (friendOpt.isPresent()) {
            Friend f = friendOpt.get();
            if (f.getUser2().getId().equals(currentUser.getId())) {
                f.setStatus(FriendStatus.accepted);
                friendService.save(f);
            }
        }
        return "redirect:/app/profile";
    }

    // --- REFUSER UNE DEMANDE ---
    @PostMapping("/app/friends/{id}/reject")
    public String rejectFriend(@PathVariable Integer id, Model model) {
        
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/app/auth";

        Optional<Friend> friendOpt = friendService.getById(id);
        if (friendOpt.isPresent()) {
            Friend f = friendOpt.get();
            if (f.getUser2().getId().equals(currentUser.getId())) {
                friendService.delete(id);
            }
        }
        return "redirect:/app/profile";
    }
}