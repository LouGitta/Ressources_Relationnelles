package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserContextService {

    private final UserService userService;
    private final boolean devMode;
    private final Integer defaultUserId;

    @Autowired
    public UserContextService(
            UserService userService,
            @Value("${dev.mode:false}") boolean devMode,
            @Value("${dev.mode.default-user-id:3}") Integer defaultUserId) {
        this.userService = userService;
        this.devMode = devMode;
        this.defaultUserId = defaultUserId;
    }

    public boolean isConnected(Principal principal) {
        return principal != null || devMode;
    }

    public Optional<User> getCurrentUser(Principal principal) {
        if (devMode && principal == null) {
            return userService.getById(defaultUserId);
        } else if (principal != null) {
            return userService.getByUsername(principal.getName());
        }
        return Optional.empty();
    }
}