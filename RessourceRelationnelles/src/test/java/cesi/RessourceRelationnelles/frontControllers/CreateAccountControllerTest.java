package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.config.Routes;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAccountControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private CreateAccountController createAccountController;

    @Test
    public void testAfficherCreateAccount() {
        String viewName = createAccountController.afficherCreateAccount();
        assertEquals("createAccount", viewName);
    }

    @Test
    public void testRegisterUser_Success() {
        when(userService.getByUsername("newUser")).thenReturn(Optional.empty());
        when(userService.getByEmail("new@email.com")).thenReturn(Optional.empty());
        when(userService.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = createAccountController.registerUser(
                "newUser",
                "new@email.com",
                "strongPassword123",
                "strongPassword123",
                model
        );

        assertEquals(Routes.REDIRECT_LOGIN, viewName);
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_PasswordMismatch() {
        String viewName = createAccountController.registerUser(
                "newUser",
                "new@email.com",
                "strongPassword123",
                "differentPassword",
                model
        );

        assertEquals("createAccount", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        when(userService.getByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        String viewName = createAccountController.registerUser(
                "existingUser",
                "new@email.com",
                "strongPassword123",
                "strongPassword123",
                model
        );

        assertEquals("createAccount", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("Le nom d'utilisateur est déjà utilisé"));
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("existing@email.com");
        when(userService.getByUsername("newUser")).thenReturn(Optional.empty());
        when(userService.getByEmail("existing@email.com")).thenReturn(Optional.of(existingUser));

        String viewName = createAccountController.registerUser(
                "newUser",
                "existing@email.com",
                "strongPassword123",
                "strongPassword123",
                model
        );

        assertEquals("createAccount", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("Cette adresse email est déjà enregistrée"));
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        String viewName = createAccountController.registerUser(
                "newUser",
                "invalid-email",
                "strongPassword123",
                "strongPassword123",
                model
        );

        assertEquals("createAccount", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_ShortPassword() {
        String viewName = createAccountController.registerUser(
                "newUser",
                "new@email.com",
                "short",
                "short",
                model
        );

        assertEquals("createAccount", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(userService, never()).save(any(User.class));
    }
}
