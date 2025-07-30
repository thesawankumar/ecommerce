package thesawan.in.ecommerce.response;

import lombok.Data;
import thesawan.in.ecommerce.domain.USER_ROLE;

@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private USER_ROLE role;
}
