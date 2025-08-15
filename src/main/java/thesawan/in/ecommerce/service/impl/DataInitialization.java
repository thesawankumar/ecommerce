package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUserName = "sawankushwaha249@gmail.com";

        if (userRepository.findByEmail(adminUserName) == null) {
            User admin = new User();
            admin.setPassword(passwordEncoder.encode("Sawan#1234"));
            admin.setFullName("Sawan");
            admin.setEmail(adminUserName);
            admin.setRole(USER_ROLE.ROLE_ADMIN);

            userRepository.save(admin);

        }
    }
}
