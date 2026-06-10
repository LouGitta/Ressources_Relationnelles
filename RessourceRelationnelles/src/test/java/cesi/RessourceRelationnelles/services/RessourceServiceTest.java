package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.repositories.RessourceRepository;
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
public class RessourceServiceTest {

    @Mock
    private RessourceRepository ressourceRepository;

    @Mock
    private UserService userService;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private RessourceService ressourceService;

    private User author;
    private User viewer;
    private Ressource ressource;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1);
        
        viewer = new User();
        viewer.setId(2);

        ressource = new Ressource();
        ressource.setId(10);
        ressource.setUser(author);
        ressource.setStatus(RessourceStatus.published);
        ressource.setVisibility(Visibility.public_visibility);
    }

    @Test
    void testGetById() {
        when(ressourceRepository.findById(10)).thenReturn(Optional.of(ressource));
        Optional<Ressource> result = ressourceService.getById(10);
        assertTrue(result.isPresent());
        assertEquals(10, result.get().getId());
    }

    @Test
    void testCanViewRessource_Public() {
        when(ressourceRepository.findById(10)).thenReturn(Optional.of(ressource));
        assertTrue(ressourceService.canViewRessource(2, 10));
    }

    @Test
    void testCanViewRessource_PrivateAsAuthor() {
        ressource.setVisibility(Visibility.private_visibility);
        when(ressourceRepository.findById(10)).thenReturn(Optional.of(ressource));
        assertTrue(ressourceService.canViewRessource(1, 10));
        assertFalse(ressourceService.canViewRessource(2, 10));
    }

    @Test
    void testCanViewRessource_Shared() {
        ressource.setVisibility(Visibility.shared);
        when(ressourceRepository.findById(10)).thenReturn(Optional.of(ressource));
        when(friendService.areFriends(2, 1)).thenReturn(true);
        assertTrue(ressourceService.canViewRessource(2, 10));
    }

    @Test
    void testIsOwnerOrModerator() {
        when(ressourceRepository.findById(10)).thenReturn(Optional.of(ressource));
        assertTrue(ressourceService.isOwnerOrModerator(1, 10)); // Owner
        
        when(userService.isModerator(2)).thenReturn(true);
        assertTrue(ressourceService.isOwnerOrModerator(2, 10)); // Moderator
    }
}
