package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Comment;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    private User author;
    private User otherUser;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1);

        otherUser = new User();
        otherUser.setId(2);

        comment = new Comment();
        comment.setId(100);
        comment.setUser(author);
        comment.setContent("Hello testing!");
    }

    @Test
    void testGetById() {
        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));
        Optional<Comment> result = commentService.getById(100);
        assertTrue(result.isPresent());
        assertEquals("Hello testing!", result.get().getContent());
    }

    @Test
    void testCanDeleteComment_Author() {
        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));
        assertTrue(commentService.canDeleteComment(1, 100)); // Author
    }

    @Test
    void testCanDeleteComment_NonAuthorNonModerator() {
        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));
        when(userService.isModerator(2)).thenReturn(false);
        assertFalse(commentService.canDeleteComment(2, 100)); // Other user
    }

    @Test
    void testCanDeleteComment_Moderator() {
        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));
        when(userService.isModerator(2)).thenReturn(true);
        assertTrue(commentService.canDeleteComment(2, 100)); // Moderator
    }
}
