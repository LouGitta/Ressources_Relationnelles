package cesi.RessourceRelationnelles.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RessourceService ressourceService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    void testIsModeratorOrAbove() {
        when(userService.isModerator(1)).thenReturn(true);
        assertTrue(permissionService.isModeratorOrAbove(1));

        when(userService.isModerator(2)).thenReturn(false);
        assertFalse(permissionService.isModeratorOrAbove(2));
        
        assertFalse(permissionService.isModeratorOrAbove(null));
    }

    @Test
    void testCanViewRessource() {
        when(ressourceService.canViewRessource(1, 100)).thenReturn(true);
        assertTrue(permissionService.canViewRessource(1, 100));

        when(ressourceService.canViewRessource(2, 100)).thenReturn(false);
        assertFalse(permissionService.canViewRessource(2, 100));
        
        assertFalse(permissionService.canViewRessource(1, null));
    }

    @Test
    void testCanModifyRessource() {
        when(ressourceService.isOwnerOrModerator(1, 100)).thenReturn(true);
        assertTrue(permissionService.canModifyRessource(1, 100));

        when(ressourceService.isOwnerOrModerator(2, 100)).thenReturn(false);
        assertFalse(permissionService.canModifyRessource(2, 100));
        
        assertFalse(permissionService.canModifyRessource(null, 100));
    }

    @Test
    void testCanDeleteComment() {
        when(commentService.canDeleteComment(1, 200)).thenReturn(true);
        assertTrue(permissionService.canDeleteComment(1, 200));

        when(commentService.canDeleteComment(2, 200)).thenReturn(false);
        assertFalse(permissionService.canDeleteComment(2, 200));
        
        assertFalse(permissionService.canDeleteComment(null, 200));
    }
}
