package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.UserRepository;
import thesawan.in.ecommerce.response.SignUpRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<User> createUserHandler(@RequestBody SignUpRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);

    }
}
