package cesi.RessourceRelationnelles.frontControllers;

import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Transactional
@Controller
public class CreateAccountController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateAccountController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/app/create-account")
    public String afficherCreateAccount() {
        return "createAccount";
    }

    @PostMapping("/app/create-account")
    public String createAccount(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        // 1) Passwords match
        if (!password.equals(confirmPassword)) {
            return "redirect:/app/create-account?error=password_mismatch";
        }

        // 2) Unique checks
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/app/create-account?error=email_exists";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/app/create-account?error=username_exists";
        }

        // 3) Create user
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password)); // IMPORTANT: BCrypt
        u.setRole(Role.CITIZEN);
        u.setActive(true);
        System.out.println("Creating user email=" + email);
        System.out.println("Saved user id=" + u.getId());
        userRepository.save(u);

        // Version A: redirect to login (with small flag)
        return "redirect:/app/login?registered";
    }
}