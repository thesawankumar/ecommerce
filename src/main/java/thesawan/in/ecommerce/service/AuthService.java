package thesawan.in.ecommerce.service;

import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.response.AuthResponse;
import thesawan.in.ecommerce.response.LoginRequest;
import thesawan.in.ecommerce.response.SignUpRequest;


public interface AuthService {
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignUpRequest req) throws Exception;
    AuthResponse sign_in(LoginRequest req) throws Exception;
}
