package cesi.RessourceRelationnelles.security;

import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> get(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return Optional.empty();
        String email = authentication.getName();          // login = email
        return userRepository.findByEmail(email);
    }
}