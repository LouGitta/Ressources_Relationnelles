package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class CommentFrontController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RessourceService ressourceService;

    // --- AJOUTER UN COMMENTAIRE ---
    @PostMapping("/app/ressource/{id}/comments")
    public String addComment(@PathVariable("id") Integer ressourceId,
                             @RequestParam("content") String content,
                             HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/app/auth"; 
        }
        Optional<User> userOpt = userService.getByUsername(principal.getName());
        
        /*
         MODE DEV : On force l'utilisateur avec l'ID 1
        Optional<User> userOpt = userService.getById(1);
        */
        
        Optional<Ressource> ressourceOpt = ressourceService.getById(ressourceId);

        if (userOpt.isPresent() && ressourceOpt.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUser(userOpt.get());
            comment.setRessource(ressourceOpt.get());
            
            commentService.save(comment);
        }

        return "redirect:/app/ressource/" + ressourceId;
    }

    // --- SUPPRIMER UN COMMENTAIRE ---
    @PostMapping("/app/comments/{id}/delete")
    public String deleteComment(@PathVariable("id") Integer commentId,
                                @RequestParam("ressourceId") Integer ressourceId,
                                HttpServletRequest request) {

        
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/app/auth";
        }
        Optional<User> userOpt = userService.getByUsername(principal.getName());
        

        /* MODE DEV : On force l'utilisateur avec l'ID 1
        Optional<User> userOpt = userService.getById(1);
        */
        Optional<Comment> commentOpt = commentService.getById(commentId);

        if (userOpt.isPresent() && commentOpt.isPresent()) {
            User currentUser = userOpt.get();
            Comment comment = commentOpt.get();

            boolean isAuthor = comment.getUser().getId().equals(currentUser.getId());
            
            boolean isModeratorOrHigher = currentUser.getRole() == Role.moderator ||
                                          currentUser.getRole() == Role.administrator ||
                                          currentUser.getRole() == Role.super_admin;

            if (isAuthor || isModeratorOrHigher) {
                commentService.delete(commentId);
            }
        }

        return "redirect:/app/ressource/" + ressourceId;
    }
}