package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.model.VerificationCode;
import thesawan.in.ecommerce.repository.UserRepository;
import thesawan.in.ecommerce.response.ApiResponse;
import thesawan.in.ecommerce.response.AuthResponse;
import thesawan.in.ecommerce.response.LoginRequest;
import thesawan.in.ecommerce.response.SignUpRequest;
import thesawan.in.ecommerce.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req) throws Exception {
        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody VerificationCode req) throws Exception {
        authService.sentLoginOtp(req.getEmail());
        ApiResponse res = new ApiResponse();
        res.setMessage("Otp Sent Successfully :)..");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.sign_in(req);
        ApiResponse res = new ApiResponse();
        res.setMessage("Sign in Successfully :)..");
        return ResponseEntity.ok(authResponse);
    }
}
