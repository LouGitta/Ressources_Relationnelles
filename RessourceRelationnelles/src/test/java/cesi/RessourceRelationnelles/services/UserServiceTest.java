package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("testUser");
        sampleUser.setEmail("test@email.com");
        sampleUser.setRole(Role.CITIZEN);
        sampleUser.setActive(true);
        sampleUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleUser));
        List<User> result = userService.getAll();
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        Optional<User> result = userService.getById(1);
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetByEmail() {
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(sampleUser));
        Optional<User> result = userService.getByEmail("test@email.com");
        assertTrue(result.isPresent());
        assertEquals("test@email.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@email.com");
    }

    @Test
    void testSave() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        User result = userService.save(sampleUser);
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(1);
        userService.delete(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testHasRole() {
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        assertTrue(userService.hasRole(1, Role.CITIZEN));
        assertFalse(userService.hasRole(1, Role.MODERATOR));
    }
}
